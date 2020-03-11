package ro.allevo.connect.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ro.allevo.fintpws.model.BaseEntity;

@XmlRootElement
public class TransactionEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String transactionId;
	private String creditorName;
	private String creditorAccount;
	private AmountEntity transactionAmount;
	private Date bookingDate;
	private Date valueDate;
	private String details;
	private IbanEntity debtorAccount;
	private String remittanceInformationUnstructured;
	
	public String getRemittanceInformationUnstructured() {
		return remittanceInformationUnstructured;
	}
	public void setRemittanceInformationUnstructured(String remittanceInformationUnstructured) {
		this.remittanceInformationUnstructured = remittanceInformationUnstructured;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public IbanEntity getDebtorAccount() {
		return debtorAccount;
	}
	public void setDebtorAccount(IbanEntity debtorAccount) {
		this.debtorAccount = debtorAccount;
	}
	@XmlElement
	public String getCreditorName() {
		return creditorName;
	}
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
	@XmlElement
	public String getCreditorAccount() {
		return creditorAccount;
	}
	public void setCreditorAccount(String creditorAccount) {
		this.creditorAccount = creditorAccount;
	}
	@XmlElement
	public AmountEntity getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(AmountEntity transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	@XmlElement
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	@XmlElement
	public Date getValueDate() {
		return valueDate;
	}
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	@XmlElement
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	@Override
	public String toString() {
		return "TransactionEntity [transactionId=" + transactionId + ", creditorName=" + creditorName
				+ ", creditorAccount=" + creditorAccount + ", transactionAmount=" + transactionAmount + ", bookingDate="
				+ bookingDate + ", valueDate=" + valueDate + ", details=" + details + "]";
	}
	
	
}
