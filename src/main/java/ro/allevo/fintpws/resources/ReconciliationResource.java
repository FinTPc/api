package ro.allevo.fintpws.resources;


import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.ReconciliationEntity;


public class ReconciliationResource extends PagedCollection<ReconciliationEntity> {

	private static final long serialVersionUID = 1L;

	public ReconciliationResource(UriInfo uriInfo, EntityManager entityManagerList) {
		super(uriInfo, 
				entityManagerList.createNamedQuery("ReconciliationEntity.findAll", ReconciliationEntity.class),
				entityManagerList.createNamedQuery("ReconciliationEntity.findTotal", Long.class),
				entityManagerList,
				ReconciliationEntity.class
				);
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@RolesAllowed(Roles.RECONCILIATION_VIEW)
	@JsonIgnore
	public PagedCollection<ReconciliationEntity> getReconciliationAsJson() {
		return this;
	}


}
