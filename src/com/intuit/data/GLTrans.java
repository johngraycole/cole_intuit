package com.intuit.data;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;
import java.math.*;
import com.intuit.ds.qb.QBIdType;

public class GLTrans implements Serializable {
	private static final long serialVersionUID = 192386256932654L;
	private String _source, _description;
	private BigDecimal _amount;
	private Calendar _date;
	private QBIdType _accountID;
	
	public GLTrans() {
		
	}

	public String getSource() {
		return _source;
	}

	public void setSource(String source) {
		_source = source;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public BigDecimal getAmount() {
		return _amount;
	}

	public void setAmount(BigDecimal amount) {
		_amount = amount;
	}

	public Calendar getDate() {
		return _date;
	}

	public void setDate(Calendar date) {
		_date = date;
	}
	
	public QBIdType getAccountID() {
		return _accountID;
	}

	public void setAccountID(QBIdType accountID) {
		_accountID = accountID;
	}

	@Override
	public String toString() {
		String str = "GLTrans: ";
		str += "<" + _description + "> ";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
		str += sdf.format(_date.getTime());
		str += " -> $" + _amount.toString();
		str += " from " + _source;
		return str;
	}
}
