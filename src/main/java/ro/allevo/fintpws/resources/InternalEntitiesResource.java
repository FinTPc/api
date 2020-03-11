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

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.InternalEntity;
import ro.allevo.fintpws.util.Roles;

/**
 * Resource class implementing /alerts path methods and acting as /alerts/{name}
 * sub-resource locator to {@link AlertResource}.
 * 
 * @author andrei
 * @version $Revision: 1.0 $
 */
public class InternalEntitiesResource extends PagedCollection<InternalEntity> {

	private EntityManager entityManagerList;
	
	public InternalEntitiesResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo,
				entityManagerList.createNamedQuery("InternalEntity.findAll", InternalEntity.class), 
				entityManagerList.createNamedQuery("InternalEntity.findTotal", Long.class),
				entityManagerList,
				InternalEntity.class
				);
		this.entityManagerList = entityManagerList;
	}

	@Path("{entityid}")
	public InternalEntityResource getInternalEntity(@PathParam("entityid") long entityId) {
		return new InternalEntityResource(getUriInfo(), getEntityManager(), entityId);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.INTERNAL_ENTITIES_LIST_VIEW)
	@JsonIgnore
	public PagedCollection<InternalEntity> getInternalEntitiesAsJson() {
		return this;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	@Path("/byRights")
	public List<String> getInternalEntitiesByRights(@QueryParam("messageType") String messageType, @Context HttpServletRequest request) {
		Integer userId = (Integer)request.getSession().getAttribute("user_id");
		TypedQuery<String> internalEntitites = this.entityManagerList.createNamedQuery("InternalEntity.findByRights", String.class)
				.setParameter("messageType", messageType)
				.setParameter("userId", userId);
		return internalEntitites.getResultList();
	}
	
	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.INTERNAL_ENTITIES_LIST_MODIFY)
	public Response postForm(InternalEntity internalEntity) {
		return post(internalEntity);
	}
}
