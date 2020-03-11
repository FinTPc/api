package ro.allevo.fintpws.resources;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ro.allevo.fintpws.util.URIFilter;
import ro.allevo.fintpws.util.enums.BusinessAreaToViews;

public class MessagesByBusinessAreaResource extends PagedCollection {
	@JsonProperty(value = "items")
	public List<Object> getMessages() {
		return getItemsObjects();
	}
	
	public MessagesByBusinessAreaResource(UriInfo uriInfo, EntityManager entityManager, BusinessAreaToViews businessArea, URIFilter... extraFilters) {
		super(uriInfo, businessArea, extraFilters);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnore
	public PagedCollection getMethod(@Context HttpServletRequest request) {
		setRequest(request);
		getItems();
		return this;
	}
}
