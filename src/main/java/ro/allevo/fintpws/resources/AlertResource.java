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

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.AlertEntity;

/**
 * Resource class implementing /alerts/{name} path methods.
 * 
 * @author costi
 * @version $Revision: 1.0 $
 */
public class AlertResource extends BaseResource<AlertEntity> {
	
	public AlertResource(UriInfo uriInfo, EntityManager entityManagerConfig, String alertName) {
		super(AlertEntity.class, uriInfo, entityManagerConfig, alertName);
	}

	/**
	 * GET method : returns an application/json formatted alert
	 * 
	 * @return JSONObject the alert
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AlertEntity getAlert() {
		return get();
	}

	/**
	 * PUT method : updates the alert
	 * 
	 * @param jsonEntity
	 *            JSONObject the alert holding new values
	 * @return Response
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAlert(AlertEntity entity) {
		return put(entity);
	}

	/**
	 * DELETE method : deletes the alert
	 * 
	 * @return Response
	 */
	@DELETE
	public Response deleteAlert() {
		return delete();
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	public String toString() {
		return get().getAlertname();
	}
}
