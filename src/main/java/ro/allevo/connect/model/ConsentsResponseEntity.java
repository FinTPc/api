package ro.allevo.connect.model;

public class ConsentsResponseEntity {
	private String consentStatus;
	private String consentId;
	private Links _links;
	
	public String getConsentStatus() {
		return consentStatus;
	}
	public void setConsentStatus(String consentStatus) {
		this.consentStatus = consentStatus;
	}
	public String getConsentId() {
		return consentId;
	}
	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}
	public Links get_links() {
		return _links;
	}
	public void set_links(Links _links) {
		this._links = _links;
	}
}


