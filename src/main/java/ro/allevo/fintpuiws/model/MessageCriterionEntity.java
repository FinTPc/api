package ro.allevo.fintpuiws.model;

import java.io.IOException;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ro.allevo.fintpuiws.util.DatasourceParser;

@Entity
@Table(schema = "FINCFG", name = "reportmessagecriteria")
@NamedQueries({
	@NamedQuery(name = "MessageCriterionEntity.findByBusinessArea", query = "SELECT c FROM MessageCriterionEntity c"
			+ " where c.businessArea=:businessarea order by c.displayOrder")
	})
public class MessageCriterionEntity {

	@Id
	private long id;
	
	private String label;
	
	@Column(name = "displayorder")
	private long displayOrder;
	
	@Column(name = "messagetypesbusinessarea")
	private String businessArea;
	
	private String type;
	
	private String datasource;
	
	private String field;
	
	@Transient
	private Map<String, String> values;
	
	private String masterlabel;
	
	public String getMasterlabel() {
		return masterlabel;
	}

	public void setMasterlabel(String masterlabel) {
		this.masterlabel = masterlabel;
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public Map<String, String> getDatasource() {
		return values;
	}
	
	public void fetchDatasource(EntityManager entityManager) throws JsonParseException, JsonMappingException, IOException {
		values = DatasourceParser.parseAndFetch(datasource, entityManager);
	}

	public String getField() {
		return field;
	}
}
