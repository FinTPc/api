package ro.allevo.connect.model;

import java.sql.Timestamp;
import java.util.Date;

public class BalanceEntity {
	
	private String balanceType;
	private Timestamp lastChangeDateTime;
	private Date referenceDate;
	private String lastCommittedTransaction;
	private AmountEntity balanceAmount;
	
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
	public Timestamp getLastChangeDateTime() {
		return lastChangeDateTime;
	}
	public void setLastChangeDateTime(Timestamp lastChangeDateTime) {
		this.lastChangeDateTime = lastChangeDateTime;
	}
	public Date getReferenceDate() {
		return referenceDate;
	}
	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}
	public String getLastCommittedTransaction() {
		return lastCommittedTransaction;
	}
	public void setLastCommittedTransaction(String lastCommittedTransaction) {
		this.lastCommittedTransaction = lastCommittedTransaction;
	}
	public AmountEntity getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(AmountEntity balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
			
}
