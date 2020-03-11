package ro.allevo.connect.resources;


import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.RequestBody;

import ro.allevo.connect.model.AmqMessage;
import ro.allevo.fintpws.util.JsonResponseWrapper;


public class JmsResources{

	
	private JmsTemplate jmsTemplate;
	private ApplicationContext context;
	private String queueName;
	
	public JmsResources(ApplicationContext context, JmsTemplate jmsTemplate, String queueName){
		this.context = context;
		this.jmsTemplate = jmsTemplate;
		this.queueName = queueName;
	}
	
	
	@POST
	@Path("sendToAmq")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sendToAmq(AmqMessage messageAmq) {
		try {
			if(jmsTemplate!=null) {
				jmsTemplate.send(this.queueName, messageCreator -> {
					TextMessage message = messageCreator.createTextMessage(messageAmq.getText());
					message.setJMSCorrelationID(messageAmq.getId());
					return message;
				});
				
			}else{
				return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "");
			}
		}catch(Exception e) {
			e.printStackTrace();
			return JsonResponseWrapper.getResponse(Response.Status.INTERNAL_SERVER_ERROR, "");
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, "");
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Path("send")
	public String sendMessage(@RequestBody String xml) {
		try {
			if(jmsTemplate!=null) {
				jmsTemplate.send("queueName1", messageCreator -> {
					TextMessage message = messageCreator.createTextMessage(xml);
					message.setJMSMessageID("5e39213c-efac5082-9ea50014");
					message.setJMSCorrelationID("5e39213c-efac5082-9ea50014");
					return message;
				});
			}else{
				return "error 1";
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "error 2";
		}
		return "ok";
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("stop")
	public Response stop() {
		JmsListenerEndpointRegistry jms1 =  context.getBean(JmsListenerEndpointRegistry.class);
		for(MessageListenerContainer stock : jms1.getListenerContainers()){
			stock.stop();
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, "Jms Listener Stopped");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("start")
	public Response start() throws NamingException{
		
		JmsListenerEndpointRegistry jms1 = null;
		if(context!=null && context.getBean(JmsListenerEndpointRegistry.class) != null) {
			jms1 = context.getBean(JmsListenerEndpointRegistry.class);
			jms1.start();
		}
		/*JmsListenerEndpointRegistry customRegistry = context.getBean("jmsRegistry", JmsListenerEndpointRegistry.class);
		customRegistry.start();*/
		return JsonResponseWrapper.getResponse(Response.Status.OK, "Jms Listener started");
	}

}