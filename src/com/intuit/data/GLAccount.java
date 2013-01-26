package com.intuit.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.intuit.ds.qb.*;


public class GLAccount {
	private QBAccount _acct;
	private BigDecimal _begBal, _endBal;
	private BigDecimal _positives, _negatives;

	public GLAccount(QBAccount acct) {
		_acct = acct;
	}
	
	public String getAcctNum() {
		return _acct.getAcctNum();
	}
	
	public String getName() {
		return _acct.getName();
	}

	public BigDecimal getBeginBalance() {
		return _begBal;
	}

	public void setBeginBalance(BigDecimal begBal) {
		_begBal = begBal;
	}

	public BigDecimal getEndBalance() {
		return _endBal;
	}

	public void setEndBalance(BigDecimal endBal) {
		_endBal = endBal;
	}
	
	public BigDecimal getPositives() {
		return _positives;
	}

	public void setPositives(BigDecimal positives) {
		_positives = positives;
	}

	public BigDecimal getNegatives() {
		return _negatives;
	}

	public void setNegatives(BigDecimal negatives) {
		_negatives = negatives;
	}
}
