package ro.allevo.connect.model;

import java.io.Serializable;

import ro.allevo.fintpws.model.BaseEntity;

public class AccountWrapperEntity extends BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountEntity account;

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}
	
}
