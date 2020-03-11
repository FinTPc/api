package ro.allevo.at.resources;

import javax.persistence.EntityManager;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.allevo.at.model.TxProcessingTestLogEntity;
import ro.allevo.fintpws.resources.PagedCollection;

public class TxProcessingTestLogResource extends PagedCollection<TxProcessingTestLogEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TxProcessingTestLogResource(UriInfo uriInfo, EntityManager entityManagerAutoTesting) {
		super(uriInfo,
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestLogEntity.findAll", TxProcessingTestLogEntity.class),
				entityManagerAutoTesting.createNamedQuery("TxProcessingTestLogEntity.findTotal", Long.class),
				entityManagerAutoTesting,
				TxProcessingTestLogEntity.class);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertTxProcessingTestLogEntity(TxProcessingTestLogEntity txProcessingTestLogEntity) {
		return post(txProcessingTestLogEntity);
	}

}
