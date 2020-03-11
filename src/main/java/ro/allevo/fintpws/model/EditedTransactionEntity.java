package ro.allevo.fintpws.model;

import java.io.Serializable;
import javax.persistence.*;

import ro.allevo.fintpws.util.annotations.URLId;


/**
 * The persistent class for the editedtransactions database table.
 * 
 */
@Entity
@Table(name="editedtransactions",schema="finbo")
@NamedQuery(name="EditedTransactionEntity.findAll", query="SELECT e FROM EditedTransactionEntity e")
public class EditedTransactionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@URLId
	@GeneratedValue(generator="EditedTransactionIdGenerator")
	@TableGenerator(name="EditedTransactionIdGenerator", table="FINCFG.IDGENLIST",
		pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
		pkColumnValue="EDITEDTRANSACTIONS_ID")
	
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(length=32)
	private String correlationid;

	@Column(length=2147483647)
	private String payload;

	private Integer status;

	private Integer userid;

	public EditedTransactionEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCorrelationid() {
		return this.correlationid;
	}

	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}

	public String getPayload() {
		return this.payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

}