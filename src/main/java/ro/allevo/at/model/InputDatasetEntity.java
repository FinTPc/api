package ro.allevo.at.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BaseEntity;

import java.util.List;


/**
 * The persistent class for the inputdatasets database table.
 * 
 */
@Entity
@Table(schema = "FINAT", name="inputdatasets")
@NamedQueries({
	@NamedQuery(name="InputDatasetEntity.findAllById", query="SELECT i FROM InputDatasetEntity i WHERE i.interfaceconfig.id=:id"),
	@NamedQuery(name ="InputDatasetEntity.findTotal", query = "select count(i.id) from InputDatasetEntity i")

	})
public class InputDatasetEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer id;

	private String dataset;

	private String datasettype;

	//bi-directional many-to-one association to ExpectedOutputDatasetEntity
	@OneToMany(mappedBy="inputdataset")
	private List<ExpectedOutputDatasetEntity> expectedoutputdatasets;

	//bi-directional many-to-one association to InterfaceConfigEntity
	@ManyToOne
	@JoinColumn(name="interfaceconfigid")
	private InterfaceConfigEntity interfaceconfig;

	//bi-directional many-to-one association to TxProcessingTestLogEntity
	@OneToMany(mappedBy="inputdataset")
	private List<TxProcessingTestLogEntity> txprocessingtestlogs;

	public InputDatasetEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDataset() {
		return this.dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public String getDatasettype() {
		return this.datasettype;
	}

	public void setDatasettype(String datasettype) {
		this.datasettype = datasettype;
	}
	@JsonIgnore
	public List<ExpectedOutputDatasetEntity> getExpectedoutputdatasets() {
		return this.expectedoutputdatasets;
	}

	public void setExpectedoutputdatasets(List<ExpectedOutputDatasetEntity> expectedoutputdatasets) {
		this.expectedoutputdatasets = expectedoutputdatasets;
	}

	public ExpectedOutputDatasetEntity addExpectedoutputdataset(ExpectedOutputDatasetEntity expectedoutputdataset) {
		getExpectedoutputdatasets().add(expectedoutputdataset);
		expectedoutputdataset.setInputdataset(this);

		return expectedoutputdataset;
	}

	public ExpectedOutputDatasetEntity removeExpectedoutputdataset(ExpectedOutputDatasetEntity expectedoutputdataset) {
		getExpectedoutputdatasets().remove(expectedoutputdataset);
		expectedoutputdataset.setInputdataset(null);

		return expectedoutputdataset;
	}

	public InterfaceConfigEntity getInterfaceconfig() {
		return this.interfaceconfig;
	}

	public void setInterfaceconfig(InterfaceConfigEntity interfaceconfig) {
		this.interfaceconfig = interfaceconfig;
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
		txprocessingtestlog.setInputdataset(this);

		return txprocessingtestlog;
	}

	public TxProcessingTestLogEntity removeTxprocessingtestlog(TxProcessingTestLogEntity txprocessingtestlog) {
		getTxprocessingtestlogs().remove(txprocessingtestlog);
		txprocessingtestlog.setInputdataset(null);

		return txprocessingtestlog;
	}

}