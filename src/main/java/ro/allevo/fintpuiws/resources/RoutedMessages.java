package ro.allevo.fintpuiws.resources;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ro.allevo.fintpuiws.model.RoutedMessagesEntity;
import ro.allevo.fintpws.exceptions.ApplicationJsonException;

public class RoutedMessages {

	private final EntityManager entityManager;
	private final Logger logger = LogManager.getLogger(getClass().getName());
	
	public RoutedMessages(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Path("{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRoutedMessage(RoutedMessagesEntity routedMessagesEntity) throws JsonParseException, JsonMappingException, IOException {
		try {
			EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
			RoutedMessagesEntity rm = em.find(RoutedMessagesEntity.class, routedMessagesEntity.getId());
			rm.setPaymentid(routedMessagesEntity.getPaymentid());
			em.getTransaction().begin();
			em.merge(rm);
			em.getTransaction().commit();
			em.close();
		} catch (RollbackException re) {
			ApplicationJsonException.handleSQLException(re, "Error updating", logger, routedMessagesEntity.getClass());
			throw re;
		}
		return Response.status(HttpStatus.OK.value()).entity(null).build();
	}
}
