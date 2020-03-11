package ro.allevo.connect.model;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.allevo.fintpws.model.BaseEntity;

@Entity
@Table(schema = "finconnect", name = "consents")
@Cacheable(false)
@NamedQueries({ @NamedQuery(name = "ConsentEntity.findAll", query = "select b from ConsentEntity b"),
		@NamedQuery(name = "ConsentEntity.findTotal", query = "select count(b.bic) from ConsentEntity b"),
		@NamedQuery(name = "ConsentEntity.findById", query = "select b from ConsentEntity b where b.bic=:id")})

public class ConsentEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bic")
	private String bic;

	@Column(name = "consent_id")
	private String consentId;

	@Column(name = "valid_until")
	private String validUntil;

	@Column(name = "url")
	private String url;

	//@OneToOne(mappedBy = "consentEntity")
	@OneToOne(targetEntity = AuthorizationServersEntity.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "BIC", referencedColumnName = "BIC", insertable = false, updatable = false)
	
	private AuthorizationServersEntity authorizationServersEntity;

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getConsentId() {
		return consentId;
	}

	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public String getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonIgnore
	public AuthorizationServersEntity getConnec() {
		return authorizationServersEntity;
	}

	@JsonIgnore
	public void setConnec(AuthorizationServersEntity connec) {
		this.authorizationServersEntity = connec;
	}

}