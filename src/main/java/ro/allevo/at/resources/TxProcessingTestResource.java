package ro.allevo.at.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.MediaType;

import ro.allevo.at.model.TxProcessingTestEntity;
import ro.allevo.fintpws.resources.PagedCollection;

public class TxProcessingTestResource extends PagedCollection<TxProcessingTestEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TxProcessingTestResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting) {
		super(uriInfo,
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestEntity.findAll", TxProcessingTestEntity.class),
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestEntity.findTotal", Long.class),
				entityManagerAutoTesting,
				TxProcessingTestEntity.class);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection<TxProcessingTestEntity> getTxProcessingTestEntitiesAsJson() {
		return this;
	}
	
//	@Path("{id}")
//	public TxProcessingTestEntity getTxProcessingTestEntity(@PathParam("id") Integer id) {
//		return get
//	}
	
	@Path("{id}/datasetinput")
	public InputDatasetResource getInputDatasetResource(@PathParam("id") Integer id){
		return new InputDatasetResource(getUriInfo(), getEntityManager(), id);
	}
	
	@Path("processing-log")
	public TxProcessingTestLogResource getTxProcessingTestLogResource(){
		return new TxProcessingTestLogResource(getUriInfo(), getEntityManager());
	}
	
//	@Path("{id}/datasetoutput")
//	public ExpectedOutputDatasetResource getExpectedOutputDatasetResource(){
//		return new ExpectedOutputDatasetResource(uriInfo, queryProvider, extraFilters);
//	}

//	@Path("{id}/interface-configs")
//	public InterfaceConfigResource getInterfaceConfigResource(){
//		return new InterfaceConfigResource();
//	}

}
