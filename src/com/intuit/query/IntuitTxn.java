package com.intuit.query;

import java.math.BigDecimal;
import java.util.Calendar;

public class IntuitTxn {
	public enum TxnType {
		BillPayment,
		Bill,
		//TODO: fill out rest of these types
		UNKNOWN
	}
	
	protected Calendar _txnDate;
	protected TxnType _txnType;
	protected String _txnDescription;
	protected BigDecimal _txnAmount;
	
	public IntuitTxn() {
		_txnDate = Calendar.getInstance();
		_txnType = TxnType.UNKNOWN;
		_txnDescription = "";
		_txnAmount = BigDecimal.ZERO;
	}

	public Calendar getTxnDate() {
		return _txnDate;
	}

	public void setTxnDate(Calendar txnDate) {
		_txnDate = txnDate;
	}

	public TxnType getTxnType() {
		return _txnType;
	}

	public void setTxnType(TxnType txnType) {
		_txnType = txnType;
	}

	public String getTxnDescription() {
		return _txnDescription;
	}

	public void setTxnDescription(String txnDescription) {
		_txnDescription = txnDescription;
	}

	public BigDecimal getTxnAmount() {
		return _txnAmount;
	}

	public void setTxnAmount(BigDecimal txnAmount) {
		_txnAmount = txnAmount;
	}

}
