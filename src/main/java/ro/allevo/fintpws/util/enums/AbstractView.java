package ro.allevo.fintpws.util.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;

import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.sessions.ArrayRecord;

import ro.allevo.fintpws.util.URIColumn;
import ro.allevo.fintpws.util.URIFilter;
import ro.allevo.fintpws.util.URIFilter.URIFilterType;
import ro.allevo.fintpws.util.URISort;

public abstract class AbstractView implements QueryProvider {
	private String name;
	private String view;
	private String schema;
	private List<String> dbColumns;
	private List<URIFilter> extraFilters = new ArrayList<URIFilter>();
	
	private EntityManager entityManager = null;
	
	//private Query query;
	private List<Object> queryParams = new ArrayList<Object>();
	
	public String getName() {
		return name;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	protected URIFilter[] getExtraFilters(HttpServletRequest request, UriInfo uriInfo) {
		return new URIFilter[] {};
	}
	
	protected AbstractView(String name, String view, String schema) {
		this.name = name;
		this.view = view;
		this.schema = schema;
	}
	
	/*public abstract Query getItemsQuery(UriInfo uriInfo, EntityManager entityManager, URIFilter... extraFilters);
	
	public abstract Query getTotalQuery(UriInfo uriInfo, EntityManager entityManager, URIFilter... extraFilters);
	
	public abstract Query getByIdQuery(EntityManager entityManager, String id);*/
	
	public Query getItemsQuery(HttpServletRequest request, UriInfo uriInfo, URIFilter... extraFilters) {
		String view = getView();
		
		if (null == view)
			return null;
		
		String query = "SELECT * FROM " + view;
		
		query = createWhereClause(query, request, uriInfo, extraFilters);
		
		query = createOrderByClause(query, uriInfo);//extraOrderBy???
		
		return getQuery(query).setHint("eclipselink.result-type", "Map");
	}
	
	public Query getTotalQuery(HttpServletRequest request, UriInfo uriInfo, URIFilter... extraFilters) {
		String view = getView();
		
		if (null == view)
			return null;
		
		String query = "SELECT count(*) FROM " + view;
		
		query = createWhereClause(query, request, uriInfo, extraFilters);
		
		return getQuery(query);
	}
	
	
	
	public Query getByIdQuery(HttpServletRequest request, String id) {
		String view = getView();
		
		if (null == view)
			return null;
		
		String query = "SELECT * FROM " + view;
		
		query = createWhereClause(query, request, null, new URIFilter("ID", URIFilterType.FILTER_TYPE_EXACT, id));
		
		return getQuery(query).setHint("eclipselink.result-type", "Map");
	}
	
	private Query getQuery(String query) {
		Query rQuery = entityManager.createNativeQuery(query);
		
		for (int j=0; j<queryParams.size(); j++)
			rQuery.setParameter(j+1, queryParams.get(j));
		
		//System.out.println("AbstractView === " + rQuery.toString());
		
		return rQuery;
	}
	
	protected String createOrderByClause(String query, UriInfo uriInfo) {
		List<URISort> sortList = URISort.parse(uriInfo, null);
		
		cleanColumns(sortList);
		
		String orderBy = "";
		
		if (sortList.size() > 0) {
			orderBy = " order by";
			
			for (URISort sort : sortList)
				orderBy += " " + sort.getName() + " " + sort.getDirection() + ", ";
			
			orderBy = orderBy.substring(0, orderBy.length() - 2);
		}
		
		return query + orderBy;
	}
	
	private String createWhereClause(String query, List<URIFilter> filters) {
		
		cleanColumns(filters);
		
		String where = " where 1=1";
		int i=0;
		
		queryParams = new ArrayList<Object>(); 
		
		//filter here
		for (URIFilter filter : filters)
			switch (filter.getType()) {
			case FILTER_TYPE_EXACT:
				where += " and " + filter.getName() + " in (?"+ ++i;
				
				for (int j=1; j<filter.getValues().size(); j++)
					where += ", ?" + ++i;
				
				where += ")";
				
				queryParams.addAll(filter.getValues());
				break;
			case FILTER_TYPE_CONTAINS:
				where += " and upper(cast(" + filter.getName() + " as varchar)) like ?"+ ++i;
				queryParams.add("%" + filter.getValue().toUpperCase() + "%");
				break;
//			case FILTER_TYPE_EXACTOR:
//				where += " or " + filter.getName() + " = ?"+ ++i;
//				queryParams.add(filter.getValue());
//				break;
//			case FILTER_TYPE_CONTAINSOR:
//				where += " and upper(" + filter.getName() + ") like ?"+ ++i;
//				queryParams.add("%" + filter.getValue().toUpperCase() + "%");
//				break;
//			case FILTER_TYPE_END:
//				//TODO - implement db specific when needed 
//				break;
			case FILTER_TYPE_LNUMERIC:
				where += " and " + filter.getName() + " >= ?" + ++i;
				queryParams.add(filter.getBigDecimal());
				break;
			case FILTER_TYPE_UNUMERIC:
				where += " and " + filter.getName() + " <= ?" + ++i;
				queryParams.add(filter.getBigDecimal());
				break;
			case FILTER_TYPE_LDATE:
				where += " and " + filter.getName() + " >= ?" + ++i;
				queryParams.add(filter.getLocalDateTime());
				
				break;
			case FILTER_TYPE_UDATE:
				where += " and " + filter.getName() + " <= ?" + ++i;
				queryParams.add(filter.getLocalDateTime());
				break;
			}
		
		return query + where;
	}
	
	protected String createWhereClause(String query, HttpServletRequest request, UriInfo uriInfo, URIFilter... extraFilters) {
		List<URIFilter> filters = new ArrayList<URIFilter>(); 
		
		if (null != uriInfo)
			filters = URIFilter.parse(uriInfo, null);
		
		filters.addAll(Arrays.asList(extraFilters));
		filters.addAll(Arrays.asList(getExtraFilters(request, uriInfo)));
		
		return createWhereClause(query, filters);
	}
	
	
	//reuse JpaFilter.cleanColumns???
	@SuppressWarnings("unchecked")
	private void cleanColumns(List<? extends URIColumn> columns) {
		//init columns
		if (null == dbColumns)
			try {
				//get first record with column names
				String checkString = "select * from " + getView();
				
				Query checkQuery = entityManager.createNativeQuery(checkString)
						.setFirstResult(0)
						.setMaxResults(1)
						.setHint("eclipselink.result-type", "Map");
				
				ArrayRecord check = ArrayRecord.class.cast(checkQuery.getSingleResult());
				
				//get column names
				dbColumns = new ArrayList<String>();
				Enumeration<DatabaseField> fields = check.keys();
				
				while (fields.hasMoreElements())
					dbColumns.add(fields.nextElement().getName());
			}
			catch (Exception e) {
				e.printStackTrace();
				//cannot get column list... disabling filters
				columns.clear();
			}
		
		//clean filters
		for (Iterator<? extends URIColumn> iter = columns.listIterator(); iter.hasNext(); ) {
		    URIColumn column = iter.next();
		    if (!dbColumns.contains(column.getName()))
		        iter.remove();
		}
	}
	
	public String getView() {
		if (null == schema || null == view)
			return null;
		
		return schema + "." + view;
	}
	
}
