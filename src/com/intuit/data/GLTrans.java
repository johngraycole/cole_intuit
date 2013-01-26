package com.intuit.data;

import java.util.*;
import java.math.*;

public class GLTrans {
	private String _source, _description;
	private BigDecimal _amount;
	private Calendar _date;
	
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
}
