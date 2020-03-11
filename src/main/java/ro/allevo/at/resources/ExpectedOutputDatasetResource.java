package ro.allevo.at.resources;

import javax.ws.rs.core.UriInfo;

import ro.allevo.at.model.ExpectedOutputDatasetEntity;
import ro.allevo.fintpws.resources.PagedCollection;
import ro.allevo.fintpws.util.URIFilter;
import ro.allevo.fintpws.util.enums.QueryProvider;

public class ExpectedOutputDatasetResource extends PagedCollection<ExpectedOutputDatasetEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpectedOutputDatasetResource(UriInfo uriInfo, QueryProvider queryProvider, URIFilter[] extraFilters) {
		super(uriInfo, queryProvider, extraFilters);
		// TODO Auto-generated constructor stub
	}

}
