package ro.allevo.at.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.at.model.InputDatasetEntity;
import ro.allevo.fintpws.resources.PagedCollection;

public class InputDatasetResource extends PagedCollection<InputDatasetEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InputDatasetResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting, Integer id) {
		super(uriInfo,
				entityManagerAutoTesting.createNamedQuery("InputDatasetEntity.findAllById", InputDatasetEntity.class).setParameter("id", id),
				entityManagerAutoTesting.createNamedQuery("InputDatasetEntity.findTotal", Long.class),
				entityManagerAutoTesting,
				InputDatasetEntity.class);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<InputDatasetEntity> getTxProcessingTestEntitiesAsJson() {
		return this;
	}

}
