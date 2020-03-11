/*
 * FinTP - Financial Transactions Processing Application
 * Copyright (C) 2013 Business Information Systems (Allevo) S.R.L.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 * or contact Allevo at : 031281 Bucuresti, 23C Calea Vitan, Romania,
 * phone +40212554577, office@allevo.ro <mailto:office@allevo.ro>, www.allevo.ro.
 */

package ro.allevo.fintpws.resources;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.codehaus.jettison.json.JSONException;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import ro.allevo.fintpws.exceptions.ApplicationJsonException;
import ro.allevo.fintpws.model.EditedTransactionEntity;
import ro.allevo.fintpws.model.FeedbackaggEntity;
import ro.allevo.fintpws.model.RoutedMessageEntity;
import ro.allevo.fintpws.util.JsonResponseWrapper;
import ro.allevo.fintpws.util.enums.MessageTypeToViews;
import org.apache.logging.log4j.Logger;

/**
 * Resource class implementing /messages/{id} path methods.
 * 
 * @author costi
 * @version $Revision: 1.0 $
 */
public class MessageResource extends BaseResource<RoutedMessageEntity> {
	
	
	private EntityManager entityManagerData;
	/**
	 * Creates a new instance of MessageResource
	 * 
	 * @param uriInfo
	 *            UriInfo actual uri passed by parent resource
	 * @param entityManagerData
	 *            EntityManager passed by parent resource
	 * 
	 * @param messageId
	 *            String
	 * @param isMessageInQueue
	 *            boolean
	 * @param needsPayload
	 *            boolean
	 * @param entityManagerConfig
	 *            EntityManager
	 */
	public MessageResource(UriInfo uriInfo, EntityManager entityManagerData, String messageId) {
		super(RoutedMessageEntity.class, uriInfo, entityManagerData, messageId);
		this.entityManagerData = entityManagerData;
	}
	
	private MessageResource(UriInfo uriInfo, EntityManager entityManagerData, TypedQuery<RoutedMessageEntity> query) { //extra getter methods
		super(RoutedMessageEntity.class, uriInfo, entityManagerData, query);
	}
	
	public static MessageResource getByCorrelationId(UriInfo uriInfo, EntityManager entityManager, String correlationId) {
		return new MessageResource(uriInfo, entityManager, 
				entityManager.createNamedQuery("RoutedMessageEntity.findByCorrelId", RoutedMessageEntity.class)
					.setParameter("id", correlationId)
				);
	}
	
	@Path("details")
	public MessageDetailsResource getMessageByMessageType() {
		Object messageId = getId();
		
		if (null == messageId)
			messageId = get().getId();
		
		String messageTypeValue = get().getMessageType();
		
		MessageTypeToViews messageType = MessageTypeToViews.get(getEntityManager(), messageTypeValue);
		
		return new MessageDetailsResource(getUriInfo(), getEntityManager(), messageId.toString(), messageType);
	}
	

	/**
	 * Method getEvents.
	 * 
	 * @return EventsResource
	 */
	@Path("events")
	public EventsResource getEvents() {
		return new EventsResource(getUriInfo(), getEntityManager(), get());
	}
	
	@Path("message-type")
	public MessageTypeResource getMessageType() {
		return new MessageTypeResource(getUriInfo(), getEntityManager(), get().getMessageType());
	}
	
	@Path("entry-queue")
	public EntryQueueResource getEntryQueue() {
		return new EntryQueueResource(getUriInfo(), getEntityManager(), get().getCorrelationId());
	}
	
	@Path("feedback")
	@GET
	public FeedbackaggEntity getFeedbackAgg() {
		String correlId = get().getCorrelationId();
		
		TypedQuery<FeedbackaggEntity> query = getEntityManager().createNamedQuery("FeedbackaggEntity.findByCorrelId", FeedbackaggEntity.class)
				.setParameter("id", correlId);
		
		List<FeedbackaggEntity> list = query.getResultList(); 
		if (list.size() > 0)
			return list.get(0);
		
		return null;
	}

	/**
	 * GET method : returns an application/json formatted message
	 * 
	 * @return JSONObject the message
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getMessage(@Context HttpServletRequest request) {
		setRequest(request);
		
		RoutedMessageEntity result = get();
		
		return result;
	}
	
	/**
	 * PUT method : updates the message
	 * 
	 * @param jsonEntity
	 *            JSONObject the message holding new values
	 * @return Response
	 * @throws JSONException
	 */
	
	@GET
	@Path("status")
	@Consumes(MediaType.APPLICATION_XML)
	public Response getStatus(@PathParam("messageId") String id, @Context HttpServletRequest request ) {
		RoutedMessageEntity message = get();
		message.getPaymentid();
		return null;
	}
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateMessage(@PathParam("messageId") String id, @Context HttpServletRequest request, String payload ) {
		Integer userId = (Integer)request.getSession().getAttribute("user_id");
		Logger logger = LogManager.getLogger(getClass().getName());
		EntityManager emd = null;
		try {
			emd = entityManagerData.getEntityManagerFactory().createEntityManager();
			emd.getTransaction().begin();
			
			StoredProcedureQuery query = emd.createNamedStoredProcedureQuery("RoutingJobEntity.createRoutingJob");
			query.setParameter("inqueuename", "Edit");
			query.setParameter("inaction", "Complete");
			query.setParameter("inmsgid", id);
			query.setParameter("inuserid", userId);
			//set params
			query.setParameter("inreason", "");
			query.setParameter("inactiondetails", "");
			query.setParameter("inmsgtype", "");
			query.setParameter("ingroupkey", "");
			query.setParameter("intimekey", "");
			for(int j=0; j<5; j++)
				query.setParameter("infield" + (j+1) + "val", "");
			
			query.execute();
			emd.flush();
			EditedTransactionEntity edit = new EditedTransactionEntity();
			edit.setCorrelationid(id);
			edit.setUserid(userId);
			edit.setPayload(payload);
			emd.persist(edit);
			
			emd.getTransaction().commit();
		}catch (RollbackException re) {
			String message = String.format(ERROR_REASON_ROLLBACK, id);
			ApplicationJsonException.handleSQLException(re, message, logger);
			throw re;
		}catch (PersistenceException re) {
			String message = String.format(ERROR_REASON_ROLLBACK, id);
			ApplicationJsonException.handleSQLException(re, message, logger);
			throw re;
		}
		finally{
			if(emd!=null)
				emd.close();
		}
		return JsonResponseWrapper.getResponse(Response.Status.OK, id + " updated");
	}
}
