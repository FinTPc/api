package ro.allevo.connect.schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ro.allevo.connect.model.AmqMessage;
import ro.allevo.connect.model.Transaction;
import ro.allevo.connect.model.TransactionEntity;
import ro.allevo.connect.model.TransactionsEntity;

@Component
public class AccountSatementJob implements Job {

	private List<String> resouceIds;
	private JobExecutionContext context;
	private String apiUrl;
	private String url;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		init(context);
		for(String resourceId:resouceIds){
			getStatement(resourceId);
		}
	}
	
	private void getStatement(String resourceId) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
	    builder.path("accounts/").path(resourceId).path("/transactions");
	    builder.queryParam("dateFrom", getDateFrom());
		builder.queryParam("dateTo", ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ));
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<TransactionsEntity> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,  null,  new ParameterizedTypeReference<TransactionsEntity>(){});
	    
	    TransactionsEntity transactionsEntity = response.getBody();
	    List<TransactionEntity> transactions = transactionsEntity.getBooked();
	   
	    for(TransactionEntity transaction:transactions) {
	    	StringWriter sw = new StringWriter();
	    	AmqMessage message = new AmqMessage();
	    	message.setId(transaction.getDebtorAccount().getIban());
	    	Transaction tran = new Transaction();
	    	tran.setDetails(transaction.getDetails());
	    	tran.setValueDate(transaction.getValueDate());
	    	tran.setBookingDate(transaction.getBookingDate());
	    	tran.setTransactionAmount(transaction.getTransactionAmount());
	    	tran.setCreditorAccount(transaction.getCreditorAccount());
	    	tran.setCreditorName(transaction.getCreditorName());
	    	TimeZone tz = TimeZone.getTimeZone("UTC");
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); 
	    	df.setTimeZone(tz);
	    	tran.setSystemDateTime(df.format(new Date()));
	    	tran.setFromDate(getDateFrom());
	    	tran.setToDate(ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ));
	    	tran.setAccountId(transactionsEntity.getAccount()!=null?transactionsEntity.getAccount().getIban():"");
	    	tran.setTransactionId(transaction.getTransactionId());
	    	tran.setRemittanceInformationUnstructured(transaction.getRemittanceInformationUnstructured());
	    	
	    	try {
	            JAXBContext context = JAXBContext.newInstance(Transaction.class);
	            Marshaller marshaller = context.createMarshaller();
	            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	            marshaller.marshal(tran, sw);
		    } catch (JAXBException e) {
		            e.printStackTrace();
		    }
	    	message.setText(sw.toString());
	    	restTemplate.postForEntity(apiUrl + "/connect-api/jms/sendToAmq", message, AmqMessage.class);
	    }
	}
	
	private String getDateFrom() {
		CronTrigger cronTrigger = (CronTrigger) context.getTrigger();
        String cronExpression = cronTrigger.getCronExpression();
	    CronExpression cron = null;
	    try {
			cron = new CronExpression(cronExpression);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.add(Calendar.DATE, -1);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	    return  sdf.format(cron.getTimeAfter(calendar.getTime()));
	}
	
	@SuppressWarnings("unchecked")
	private void init(JobExecutionContext context) {
		this.context = context;
		try (InputStream input = new ClassPathResource("application.properties").getInputStream()) {
            Properties prop = new Properties();
            prop.load(input);
            apiUrl = prop.getProperty("FinTP_Api.url");
	    } catch (IOException ex) {
            ex.printStackTrace();
        }

		JobDetail job = context.getJobDetail();
	    JobDataMap parameters = job.getJobDataMap();
	    this.resouceIds = (List<String>) parameters.get("resourceIds");
	    this.url = parameters.getString("url");
	    
	}

}
