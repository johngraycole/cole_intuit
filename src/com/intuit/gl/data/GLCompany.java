package com.intuit.gl.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.intuit.ds.qb.CompanyMetaData;
import com.intuit.ds.qb.QBIdType;

public class GLCompany implements Serializable {
	private static final long serialVersionUID = 897593427091201L;
    private String _qbnName, _defAPacctNum, _defARacctNum, _defBankAcctNum;
	private Calendar _fyStart, _lastUpdated;
	private List<GLAccount> _accounts;
	
	public GLCompany() {
		_accounts = new ArrayList<GLAccount>();
		_lastUpdated = Calendar.getInstance();
		_fyStart = null;
	}
	
	public GLCompany(CompanyMetaData mdata) {
		this();
		setMetadata(mdata);
	}
	
	public void setMetadata(CompanyMetaData mdata) {
	    // hard wired values until preference file is created
	    _defAPacctNum = "20000";
	    _defARacctNum = "11000";
	    _defBankAcctNum = "10600";
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

	public Calendar getLastUpdated() {
		return _lastUpdated;
	}

	public String getQbnName() {
		return _qbnName;
	}

	public void setQbnName(String qbnName) {
		_qbnName = qbnName;
	}

	public String getDefAPacctNum() {
		return _defAPacctNum;
	}

	public void setDefAPacctNum(String defAPacctNum) {
		_defAPacctNum = defAPacctNum;
	}

	public String getDefARacctNum() {
		return _defARacctNum;
	}

	public void setDefARacctNum(String defARacctNum) {
		_defARacctNum = defARacctNum;
	}

	public String getDefBankAcctNum() {
		return _defBankAcctNum;
	}

	public void setDefBankAcctNum(String defBankAcctNum) {
		_defBankAcctNum = defBankAcctNum;
	}

	public Calendar getFyStart() {
		return _fyStart;
	}

	public void setFyStart(Calendar fyStart) {
		_fyStart = fyStart;
	}

	public List<GLAccount> getAccounts() {
		return _accounts;
	}

    public QBIdType getQBAcctId(String acct) {
	for (GLAccount glacct : _accounts)  {
	    if (acct.equals(glacct.getAcctNum())) {
		//System.out.println("getQBAcctId("+acct+") -> " + glacct.getName());
		return glacct.getID();
	    }
	}
	return null;
    }

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String str = "GLCompany: " + _qbnName + "\n";
		str += "FY Start: "+ sdf.format(_fyStart.getTime()) + "\n";
		str += "Accounts:\n";
		for (GLAccount acct : _accounts)
			str += acct.toString() + "\n";
		return str;
	}
}
