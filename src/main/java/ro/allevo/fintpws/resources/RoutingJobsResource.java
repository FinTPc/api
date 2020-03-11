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

/**
 * 
 */
package ro.allevo.fintpws.resources;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.QueueEntity;
import ro.allevo.fintpws.model.RoutingJobEntity;
import ro.allevo.fintpws.model.RoutingJobParameters;
import ro.allevo.fintpws.util.JsonResponseWrapper;

/**
 * @author anda
 * @version $Revision: 1.0 $
 */

public class RoutingJobsResource extends PagedCollection<RoutingJobEntity> {
	
	private QueueEntity queueEntity; 
	
	public RoutingJobsResource(UriInfo uriInfo, EntityManager entityManagerData) {
		super(uriInfo,
				entityManagerData.createNamedQuery("RoutingJobEntity.findAll", RoutingJobEntity.class),
				entityManagerData.createNamedQuery("RoutingJobEntity.findTotal", Long.class),
				entityManagerData,
				RoutingJobEntity.class
				);
	}
	
	public RoutingJobsResource(UriInfo uriInfo, EntityManager entityManagerData, QueueEntity queueEntity) {
		this(uriInfo, entityManagerData);
		this.queueEntity = queueEntity;
	}

	
	/**
	 * Returns a routing job sub-resource with guid
	 * 
	 * @param guid
	 *            String guid of the routing job as last element in the path
	 * 
	 * @return RoutingJobResource The routing job sub-resource
	 */
	@Path("{id}")
	public RoutingJobResource getRoutingJobsResource(@PathParam("id") String guid) {
		guid = Jsoup.clean(guid, Whitelist.none());
		return new RoutingJobResource(getUriInfo(), getEntityManager(), guid);
	}

	/**
	 * GET method : returns an application/json formatted list of routing jobs
	 * 
	 * @return JSONObject The list of routing jobs
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<RoutingJobEntity> getRoutingJobsAsJson() {
		return this;
	}
	
	/**
	 * POST method : creates a routing job
	 * 
	 * @param jsonEntity
	 *            JSONObject
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRoutingJob(@Context HttpServletRequest request, @RequestParam RoutingJobParameters entity) {//calls procedure
		boolean exec = true;
		Integer userId = (Integer)request.getSession().getAttribute("user_id");
		for (String messageId : entity.getMessageIds()) {
			StoredProcedureQuery query = getEntityManager().createNamedStoredProcedureQuery("RoutingJobEntity.createRoutingJob");
			
			//set params
			query.setParameter("inqueuename", queueEntity.getName());
			query.setParameter("inaction", entity.getAction());
			query.setParameter("inreason", entity.getReason());
			query.setParameter("inactiondetails", entity.getActionDetails());
			query.setParameter("inmsgtype", entity.getMessageType());
			query.setParameter("inmsgid", messageId);
			query.setParameter("inuserid", userId);
			
			//unused
			query.setParameter("ingroupkey", "");
			query.setParameter("intimekey", "");
			
			for(int j=0; j<5; j++)
				query.setParameter("infield" + (j+1) + "val", "");
			
			exec = query.execute() && exec;
		}
		
		String[] groupKeys = entity.getGroupKeys();
		String[] timeKeys = entity.getTimeKeys();
		String[][] fieldValues = entity.getFieldValues();
		
		for (int i=0; i<entity.getGroupKeys().length; i++) {
			StoredProcedureQuery query = getEntityManager().createNamedStoredProcedureQuery("RoutingJobEntity.createRoutingJob");
			
			//set params
			query.setParameter("inqueuename", queueEntity.getName());
			query.setParameter("inaction", entity.getAction());
			query.setParameter("inreason", entity.getReason());
			query.setParameter("inactiondetails", entity.getActionDetails());
			query.setParameter("inmsgtype", entity.getMessageType());
			query.setParameter("ingroupkey", groupKeys[i]);
			query.setParameter("intimekey", timeKeys[i]);
			query.setParameter("inuserid", userId);
		
			String[] values = fieldValues[i];
			for(int j=0; j<5; j++) {
				String value = "";
				
				if (j<values.length)
					value = values[j];
				
				query.setParameter("infield" + (j+1) + "val", value);
			}
			
			//unused
			query.setParameter("inmsgid", "");
			
			
			exec = query.execute() && exec;
		}
		
		Status status = exec ? Status.OK : Status.CONFLICT;
		
		return JsonResponseWrapper.getResponse(status);
	}
}
