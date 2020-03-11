package ro.allevo.connect.amq;

import java.io.StringReader;
import java.util.Map;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ro.allevo.connect.model.PaymentEntity;
import ro.allevo.fintpuiws.model.RoutedMessagesEntity;
import ro.allevo.fintpws.config.Config;

@Component
public class MessageReceiver {

	@Context
	private UriInfo uriInfo;

	@Value("${bic.xpath.expression}")
	private String xpathExpression;
	
	@Value("${FinTP_Api.url}")
	private String apiUrl;

	@Bean
	public Config prop() {
		return new Config();
	}

	@JmsListener(destination = "#{@prop.amqPaymentIn}")
	//ToDo @Header("id") String id
	public void receiveMessage(String message, @Header("jms_correlationId") String id, @Headers Map<Object, Object> allHeaders) throws DOMException, Exception {
//		for (Map.Entry<Object,Object> entry : allHeaders.entrySet())  
//	            System.out.println("Key = " + entry.getKey() + 
//	                             ", Value = " + entry.getValue()); 
		
		Document xmlDocument = convertStringToXMLDocument(message);
		String bic = getElementByXpath(xmlDocument, xpathExpression).getTextContent();

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<String>(message);
		String baseUrl = String.format(apiUrl+"connect-api/%s/payments/pain.001-sepa-credit-transfers", bic);
		
		//3 incercari pentru  conexiune, in caz de succes incercarile se intrerup
		boolean error = true;
		for (int i = 0; i < 3; i++) {
			//apiPath
			ResponseEntity<PaymentEntity> payment = (ResponseEntity<PaymentEntity>) restTemplate.exchange(
					baseUrl, HttpMethod.POST, entity, PaymentEntity.class);
			if (payment.getStatusCodeValue() == 201) {
				String paymentId = payment.getBody().getPaymentId();
				ResponseEntity<String> authPayment = (ResponseEntity<String>) restTemplate.exchange(String.format(
						baseUrl + "/%s/authorisations", bic, paymentId), HttpMethod.POST, entity, String.class);
				if (authPayment.getStatusCodeValue() == 201) {
					authPayment = (ResponseEntity<String>) restTemplate.exchange(String.format(
							baseUrl + "/%s/status", bic, paymentId), HttpMethod.GET, entity, String.class);
					if (authPayment.getStatusCodeValue() == 200) {
						RoutedMessagesEntity rm = new RoutedMessagesEntity();
						rm.setId(id);
						rm.setPaymentid(paymentId);
						restTemplate.put(apiUrl + "/ui-api/routed-messages/"+id, rm, RoutedMessagesEntity.class);
						error = false;
						break;
					} else {
						System.out.println(authPayment.getStatusCode());
					}
				} else {
					System.out.println(authPayment.getStatusCode());
				}
			} else {
				System.out.println(payment.getStatusCode());
			}
			System.out.println("Attempt " + i + " has not passed");
		}
		if (error) {
			System.out.println("the payment has not passed");
		}
	}

	private static Node getElementByXpath(Document document, String xpathExpression) throws Exception {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			XPathExpression expr = xpath.compile(xpathExpression);
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			if (nodes != null && nodes.getLength() > 0) {
				return nodes.item(0);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			NodeList ndlst = doc.getDocumentElement().getChildNodes();
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
