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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.exceptions.ApplicationJsonException;
import ro.allevo.fintpws.model.RoutedMessageEntity;
import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.ResourcesUtils;
import ro.allevo.fintpws.util.Roles;

/**
 * Resource class implementing /events path methods and acting as /events/{id}
 * sub-resource locator.
 * 
 * @author costi
 * @version $Revision: 1.0 $
 */
public class EventsResource extends PagedCollection<StatusEntity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final String ERROR_MESSAGE_POST_EVENTS = "Error creating event : ";
	
	static final String ERROR_REASON_PARSE = "parse";
	
	public EventsResource(UriInfo uriInfo, EntityManager entityManager, RoutedMessageEntity messageEntity) {
		super(uriInfo,
				entityManager.createNamedQuery("StatusEntity.findByCorrelationId", StatusEntity.class)
					.setParameter("correlationid", (null != messageEntity)?messageEntity.getCorrelationId():null),
				entityManager.createNamedQuery("StatusEntity.findTotalByCorrelationId", Long.class)
					.setParameter("correlationid", (null != messageEntity)?messageEntity.getCorrelationId():null),
				entityManager,
				StatusEntity.class
				);
	}
	
	public EventsResource(UriInfo uriInfo, EntityManager entityManager) {
		super(uriInfo,
				entityManager.createNamedQuery("StatusEntity.findAll", StatusEntity.class),
				entityManager.createNamedQuery("StatusEntity.findTotal", Long.class),
				entityManager,
				StatusEntity.class
				);
	}
	
	/**
	 * GET method : returns an application/json formatted list of events
	 * 
	 * @return JSONObject The list of events
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<StatusEntity> getEventsAsJson() {
		return this;
	}

	/**
	 * Returns a event sub-resource named eventId
	 * 
	 * @param eventGuid
	 *            String
	 * @return EventResource The event sub-resource
	 */
	@Path("{eventId}")
	public EventResource getEvent(@PathParam("eventId") String eventGuid) {
		eventGuid = Jsoup.clean(eventGuid, Whitelist.none());
		return new EventResource(getUriInfo(), getEntityManager(), eventGuid);
	}

	
	/**
	 * POST method : creates an event
	 * 
	 * @param jsonEntity
	 *            JSONObject
	 * @return Response The URI of the newly created event
	 * @throws JSONException
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.EVENTS_MODIFY)
	public Response postForm(@Context HttpServletRequest requestContext,
			StatusEntity entity) throws JSONException {
		
		// fill required data
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String currentMoment = df.format(new Date());
		String host = requestContext.getRemoteHost();
		
		if (null == entity.getMachine())
			entity.setMachine(host);
		
		try {
			if (null == entity.getEventdate())
				entity.setEventdate(ResourcesUtils.getTimestamp(currentMoment));
			
			if (null == entity.getInsertdate())
				entity.setInsertdate(ResourcesUtils.getTimestamp(currentMoment));
		} catch (ParseException pe) {
			getLogger().error(ERROR_MESSAGE_POST_EVENTS + ERROR_REASON_PARSE, pe);
			throw new ApplicationJsonException(pe, ERROR_MESSAGE_POST_EVENTS
					+ ERROR_REASON_PARSE,
					Response.Status.BAD_REQUEST);
		}
		
		return post(entity);
	}
}
