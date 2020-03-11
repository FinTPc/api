package ro.allevo.connect.model;

import ro.allevo.fintpws.model.BaseEntity;

public class PaymentEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String paymentId;
	private String transactionStatus;
	private String psuMessage;
	private String tppMessage;
	
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getPsuMessage() {
		return psuMessage;
	}
	public void setPsuMessage(String psuMessage) {
		this.psuMessage = psuMessage;
	}
	public String getTppMessage() {
		return tppMessage;
	}
	public void setTppMessage(String tppMessage) {
		this.tppMessage = tppMessage;
	}
}
