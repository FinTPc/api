package ro.allevo.fintpws.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(schema = "FINCFG", name="USERDEFINEDROLES")
@Cacheable(false)
public class UserDefinedRoleEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="Generator")
	@TableGenerator(name="Generator", table="FINCFG.IDGENLIST",
		pkColumnName="TABCOLNAME", valueColumnName="IDVALUE",
		pkColumnValue="USERDEFINEDROLES_ID")
	@Column(unique = true, nullable = false, updatable = false)
	private long id;
	
	@Column(nullable = false, updatable = false)
	private long roleid;
	
	@Column(length = 50, name="messagetype")
	private String messageType;
	
	@Column(length = 35, name="internalentityname")
	private String internalEntityName;
	
	//low performance
	/*@OneToOne(targetEntity = MsgTypeListEntity.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "messagetype", referencedColumnName = "messagetype", insertable = false, updatable = false)
	private MsgTypeListEntity messageTypeEntity;*/
	
	/*@OneToOne(targetEntity = InternalEntity.class, cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "internalentityname", referencedColumnName = "name", insertable = false, updatable = false)
	private InternalEntity internalEntity;*/
	
	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	/*
	@JsonIgnore
	public MsgTypeListEntity getMessageTypeEntity() {
		return null;
		//return messageTypeEntity;
	}

	@JsonIgnore
	public InternalEntity getInternalEntity() {
		return null;
		//return internalEntity;
	}*/

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getInternalEntityName() {
		return internalEntityName;
	}

	public void setInternalEntityName(String internalEntityName) {
		this.internalEntityName = internalEntityName;
	}
}
