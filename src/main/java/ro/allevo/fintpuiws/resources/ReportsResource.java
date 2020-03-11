package ro.allevo.fintpuiws.resources;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ro.allevo.fintpuiws.model.FiltersEntity;
import ro.allevo.fintpuiws.model.MessageCriterionEntity;
import ro.allevo.fintpuiws.model.MessageResultEntity;
import ro.allevo.fintpuiws.model.TransactionStateEntity;
import ro.allevo.fintpws.model.MsgStatementEntity;

public class ReportsResource {

	private final EntityManager entityManager;

	public ReportsResource(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@GET
	@Path("transaction-states")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionStateEntity> getTransactionStates() {
		TypedQuery<TransactionStateEntity> query = entityManager.createNamedQuery("TransactionStateEntity.findAll",
				TransactionStateEntity.class);

		return query.getResultList();
	}

	@GET
	@Path("message-criteria")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MessageCriterionEntity> getFilters(@QueryParam("businessArea") String businessArea)
			throws JsonParseException, JsonMappingException, IOException {
		TypedQuery<MessageCriterionEntity> query = entityManager
				.createNamedQuery("MessageCriterionEntity.findByBusinessArea", MessageCriterionEntity.class)
				.setParameter("businessarea", businessArea);

		List<MessageCriterionEntity> criterias = query.getResultList();

		for (MessageCriterionEntity criterion : criterias)
			criterion.fetchDatasource(entityManager);

		return criterias;
	}

	@GET
	@Path("message-results")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MessageResultEntity> getResultHeaders(@QueryParam("businessArea") String businessArea) {
		TypedQuery<MessageResultEntity> query = entityManager
				.createNamedQuery("MessageResultEntity.findByBusinessArea", MessageResultEntity.class)
				.setParameter("businessarea", businessArea);

		return query.getResultList();
	}

	@GET
	@Path("message-statement")
	@Produces(MediaType.APPLICATION_JSON)
	public Long getCountStatement() {
		TypedQuery<Long> query = entityManager.createNamedQuery("MsgStatementEntity.findTotalDistinctMessageStatement",
				Long.class);
		return query.getResultList().get(0);
	}
	
	@GET
	@Path("filters")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FiltersEntity> getFilterFileds(@QueryParam("businessArea") String businessArea, @QueryParam("user") String user){
		TypedQuery<FiltersEntity> query = entityManager.createNamedQuery("FiltersEntity.findByBusinessAreaAndUserName", FiltersEntity.class)
				.setParameter("businessarea", businessArea)
				.setParameter("user", user);
		return query.getResultList();
	}
}
