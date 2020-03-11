package ro.allevo.at.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.util.annotations.URLId;

import java.util.List;


/**
 * The persistent class for the txprocessingtests database table.
 * 
 */
@Entity
@Table(schema = "FINAT", name="txprocessingtests")
@NamedQueries({
	@NamedQuery(name="TxProcessingTestEntity.findAll", query="SELECT t FROM TxProcessingTestEntity t"),
	@NamedQuery(name = "TxProcessingTestEntity.findTotal", query = "select count(t.id) from TxProcessingTestEntity t")

	})
public class TxProcessingTestEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, updatable=false)
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer id;
	
	@Column
	private String description;

	@Column(name = "name", nullable = false, length = 500)
	private String name;

	@Column
	private Integer outputinterfaceid;

	@Column(length = 30)
	private String txtype;

	//bi-directional many-to-one association to TxProcessingTestLogEntity
	@OneToMany(mappedBy="txprocessingtest")
	private List<TxProcessingTestLogEntity> txprocessingtestlogs;

	//bi-directional many-to-one association to InterfaceConfigEntity
	@ManyToOne
	@JoinColumn(name="inputinterfaceid")
	private InterfaceConfigEntity interfaceconfig;

	public TxProcessingTestEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getOutputinterfaceid() {
		return this.outputinterfaceid;
	}

	public void setOutputinterfaceid(Integer outputinterfaceid) {
		this.outputinterfaceid = outputinterfaceid;
	}

	public String getTxtype() {
		return this.txtype;
	}

	public void setTxtype(String txtype) {
		this.txtype = txtype;
	}

	@JsonIgnore
	public List<TxProcessingTestLogEntity> getTxprocessingtestlogs() {
		return this.txprocessingtestlogs;
	}

	public void setTxprocessingtestlogs(List<TxProcessingTestLogEntity> txprocessingtestlogs) {
		this.txprocessingtestlogs = txprocessingtestlogs;
	}

	public TxProcessingTestLogEntity addTxprocessingtestlog(TxProcessingTestLogEntity txprocessingtestlog) {
		getTxprocessingtestlogs().add(txprocessingtestlog);
		txprocessingtestlog.setTxprocessingtest(this);

		return txprocessingtestlog;
	}

	public TxProcessingTestLogEntity removeTxprocessingtestlog(TxProcessingTestLogEntity txprocessingtestlog) {
		getTxprocessingtestlogs().remove(txprocessingtestlog);
		txprocessingtestlog.setTxprocessingtest(null);

		return txprocessingtestlog;
	}

	public InterfaceConfigEntity getInterfaceconfig() {
		return this.interfaceconfig;
	}

	public void setInterfaceconfig(InterfaceConfigEntity interfaceconfig) {
		this.interfaceconfig = interfaceconfig;
	}

}