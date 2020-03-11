package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import ro.allevo.fintpws.util.annotations.URLId;

import java.util.List;


/**
 * The persistent class for the txtemplatesconfig database table.
 * 
 */
@Entity
@Table(name="txtemplatesconfig")
@NamedQueries({
	@NamedQuery(name="TxTemplatesConfigEntity.findAll", query="SELECT t FROM TxTemplatesConfigEntity t order by t.id"),
	@NamedQuery(name="TxTemplatesConfigEntity.findTotal", query="SELECT count(t.id) FROM TxTemplatesConfigEntity t"),
	@NamedQuery(name="TxTemplatesConfigEntity.findById", query="SELECT t FROM TxTemplatesConfigEntity t where t.id=:id")
})
@Cacheable(false)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TxTemplatesConfigEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@URLId
	@GeneratedValue(generator="TXTEMPLATESCONFIG_ID_GENERATOR")
	@TableGenerator(name="TXTEMPLATESCONFIG_ID_GENERATOR", table="FINCFG.IDGENLIST",
	pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
	pkColumnValue="TXTEMPLATESCONFIG_ID") 
	private Integer id;

	@Column
	private String messagetype;
	
	private String validationxsd;
	
	@Column
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonIgnore
	public String getValidationxsd() {
		return validationxsd;
	}

	public void setValidationxsd(String validationxsd) {
		this.validationxsd = validationxsd;
	}

	//bi-directional many-to-one association to TxTemplateEntity
	@OneToMany(mappedBy="txtemplatesconfig")
	private List<TxTemplatesEntity> txtemplates;

	//bi-directional many-to-one association to TxTemplatesConfigDetailedEntity
	//@JsonIgnore
	@OneToMany(mappedBy="txtemplatesconfig")
	private List<TxTemplatesConfigDetailedEntity> txtemplatesconfigdetaileds;

	public TxTemplatesConfigEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessagetype() {
		return this.messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}
	
	//@JsonManagedReference("template-config")
	@JsonIgnore
	public List<TxTemplatesEntity> getTxtemplates() {
		return this.txtemplates;
	}

	//@JsonIgnore
	public void setTxtemplates(List<TxTemplatesEntity> txtemplates) {
		this.txtemplates = txtemplates;
	}

	public TxTemplatesEntity addTxtemplate(TxTemplatesEntity txtemplates) {
		getTxtemplates().add(txtemplates);
		txtemplates.setTxtemplatesconfig(this);
		return txtemplates;
	}

	public TxTemplatesEntity removeTxtemplate(TxTemplatesEntity txtemplates) {
		getTxtemplates().remove(txtemplates);
		txtemplates.setTxtemplatesconfig(null);
		return txtemplates;
	}

	@JsonManagedReference("config-detaileds")
	public List<TxTemplatesConfigDetailedEntity> getTxtemplatesconfigdetaileds() {
		return this.txtemplatesconfigdetaileds;
	}

	
	public void setTxtemplatesconfigdetaileds(List<TxTemplatesConfigDetailedEntity> txtemplatesconfigdetaileds) {
		this.txtemplatesconfigdetaileds = txtemplatesconfigdetaileds;
	}

	public TxTemplatesConfigDetailedEntity addTxtemplatesconfigdetailed(TxTemplatesConfigDetailedEntity txtemplatesconfigdetailed) {
		getTxtemplatesconfigdetaileds().add(txtemplatesconfigdetailed);
		txtemplatesconfigdetailed.setTxtemplatesconfig(this);
		return txtemplatesconfigdetailed;
	}

	public TxTemplatesConfigDetailedEntity removeTxtemplatesconfigdetailed(TxTemplatesConfigDetailedEntity txtemplatesconfigdetailed) {
		getTxtemplatesconfigdetaileds().remove(txtemplatesconfigdetailed);
		txtemplatesconfigdetailed.setTxtemplatesconfig(null);
		return txtemplatesconfigdetailed;
	}

}