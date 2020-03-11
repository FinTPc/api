package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the txtemplatesconfigoptions database table.
 * 
 */
@Entity
@Table(name="txtemplatesconfigoptions")
@NamedQueries({
@NamedQuery(name="TxTemplatesConfigOptionEntity.findAll", query="SELECT t FROM TxTemplatesConfigOptionEntity t"),
@NamedQuery(name="TxTemplatesConfigOptionEntity.findById", query="SELECT t FROM TxTemplatesConfigOptionEntity t where t.id=:id"),
})
@Cacheable(false)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TxTemplatesConfigOptionEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@URLId
	@GeneratedValue(generator="TXTEMPLATESCONFIGOPTIONS_ID_GENERATOR")
	@TableGenerator(name="TXTEMPLATESCONFIGOPTIONS_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TXTEMPLATESCONFIGOPTIONS_ID") 
	private Integer id;

	private String datasource;

	private String description;

	private String name;

	public TxTemplatesConfigOptionEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDatasource() {
		return this.datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}