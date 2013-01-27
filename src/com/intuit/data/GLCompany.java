package com.intuit.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.intuit.ds.qb.CompanyMetaData;

public class GLCompany implements Serializable {
	private static final long serialVersionUID = 897593427091201L;
	private String _qbnName;
	private Calendar _fyStart;
	private List<GLAccount> _accounts;
	
	public GLCompany() {
		_accounts = new ArrayList<GLAccount>();
	}
	
	public GLCompany(CompanyMetaData mdata) {
		_accounts = new ArrayList<GLAccount>();
		setMetadata(mdata);
	}
	
	public void setMetadata(CompanyMetaData mdata) {
		_qbnName = mdata.getQBNRegisteredCompanyName();
		if (mdata.getFiscalYearStart() != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
				Calendar cal = Calendar.getInstance();
				cal.setTime(sdf.parse("01-01"));
				cal.add(Calendar.MONTH, mdata.getFiscalYearStart());
				_fyStart = cal;
			} catch (Exception ex) {
			}
		}
	}

	public String getQbnName() {
		return _qbnName;
	}

	public void setQbnName(String qbnName) {
		_qbnName = qbnName;
	}

	public Calendar getFyStart() {
		return _fyStart;
	}

	public void setFyStart(Calendar fyStart) {
		_fyStart = fyStart;
	}

	public List<GLAccount> get_accounts() {
		return _accounts;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String str = "GLCompany: " + _qbnName + "\n";
		str += "FY Start: "+ sdf.format(_fyStart) + "\n";
		str += "Accounts:\n";
		for (GLAccount acct : _accounts)
			str += acct.toString() + "\n";
		return str;
	}
}
