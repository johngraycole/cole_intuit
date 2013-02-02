package com.intuit.gl.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.intuit.ds.qb.QBAccount;
import com.intuit.ds.qb.QBIdType;


public class GLAccount implements Serializable {
	private static final long serialVersionUID = 9834759328710111L;
	private String _acctNum, _name;
	private BigDecimal _currBal;
	private QBIdType _id;

	private List<GLTrans> _transactions;

	public GLAccount() {
		_transactions = new ArrayList<GLTrans>();
	}
	
	public GLAccount(QBAccount acct) {
		this();
		setAccountInfo(acct);
	}
	
	public void setAccountInfo(QBAccount qb_acct) {
		if (qb_acct == null) {
			System.out.println("ERROR: GLAccount setinfo will null QBAccount");
			return;
		}
		_acctNum = qb_acct.getAcctNum();
		_name = qb_acct.getName();
		_currBal = qb_acct.getCurrentBalance();
		_id = qb_acct.getId();
	}
	
	public QBIdType getID() {
		return _id;
	}

	public void setID(QBIdType id) {
		this._id = id;
	}
	
	public void setAcctNum(String num) {
		_acctNum = num;
	}
	public void setName(String name) {
		_name = name;
	}
	
	public String getAcctNum() {
		return _acctNum;
	}
	
	public String getName() {
		return _name;
	}

	public BigDecimal getCurrentBalance() {
		return _currBal;
	}

	public void setCurrentBalance(BigDecimal bal) {
		_currBal = bal;
	}
	
	public List<GLTrans> getTransactions() {
		return _transactions;
	}
	
	@Override
	public String toString() {
		String str = "GLAccount: ";
		str += _acctNum + " - " + _name + "\n";
		if (_currBal != null)
			str += "Current Balance: $"+_currBal.toString() + "\n";
		else
			str += "Current Balance: null\n";
		str += "Transactions:\n";
		for (GLTrans txn : _transactions)
			str += "\t" + txn.toString() + "\n";
		return str;
	}
}
