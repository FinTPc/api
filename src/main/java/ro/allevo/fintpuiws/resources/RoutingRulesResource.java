package ro.allevo.fintpuiws.resources;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ro.allevo.fintpuiws.model.RoutingRulesActionEnitty;

public class RoutingRulesResource {
	
	private EntityManager entityManager;
	
	public RoutingRulesResource(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Path("actions")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoutingRulesActionEnitty> getActions() throws JsonParseException, JsonMappingException, IOException {
		TypedQuery<RoutingRulesActionEnitty> query = entityManager.createNamedQuery("RoutingRulesActionEnitty.findAll", RoutingRulesActionEnitty.class);
		
		List<RoutingRulesActionEnitty> actions = query.getResultList();
		
		for (RoutingRulesActionEnitty action : actions)
			action.fetchDatasource(entityManager);
		
		return actions;
	}
}
