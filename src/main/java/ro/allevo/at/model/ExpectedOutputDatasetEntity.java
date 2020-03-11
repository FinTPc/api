package ro.allevo.at.model;

import javax.persistence.*;

import ro.allevo.fintpws.model.BaseEntity;


/**
 * The persistent class for the expectedoutputdatasets database table.
 * 
 */
@Entity
@Table(schema = "FINAT", name="expectedoutputdatasets")
@NamedQuery(name="ExpectedOutputDatasetEntity.findAll", query="SELECT e FROM ExpectedOutputDatasetEntity e")
public class ExpectedOutputDatasetEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Integer id;

	private String dataset;

	//bi-directional many-to-one association to InputDatasetEntity
	@ManyToOne
	@JoinColumn(name="inputdatasetid")
	private InputDatasetEntity inputdataset;

	//bi-directional many-to-one association to InterfaceConfigEntity
	@ManyToOne
	@JoinColumn(name="interfaceconfigid")
	private InterfaceConfigEntity interfaceconfig;

	public ExpectedOutputDatasetEntity() {
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

	public InputDatasetEntity getInputdataset() {
		return this.inputdataset;
	}

	public void setInputdataset(InputDatasetEntity inputdataset) {
		this.inputdataset = inputdataset;
	}

	public InterfaceConfigEntity getInterfaceconfig() {
		return this.interfaceconfig;
	}

	public void setInterfaceconfig(InterfaceConfigEntity interfaceconfig) {
		this.interfaceconfig = interfaceconfig;
	}

}