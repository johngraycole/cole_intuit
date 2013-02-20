package com.intuit.gl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.intuit.gl.data.GLAccount;
import com.intuit.gl.data.GLCompany;
import com.intuit.gl.data.GLTrans;
import com.intuit.utils.ColeUtils;

public class ProcessGL {

	public static GLAccount FindAccount(GLCompany comp, String acctnum) {
		for (GLAccount acct : comp.getAccounts())  {
			if (acctnum.equals(acct.getAcctNum())) {
				System.out.println("FindAccount("+acctnum+") -> " + acct.getName());
				return acct;
			}
		}
		return null;
	}

	public static BigDecimal GetBalanceAtDate(GLAccount acct, Calendar begDate) {
		//SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
		BigDecimal curr_bal = acct.getCurrentBalance();

		//System.out.println("GetBalanceAtDate("+acct.getAcctNum()+","+sdf.format(begDate.getTime())+")");
		if (curr_bal == null) {
			//IncomeStatementPath
			curr_bal = BigDecimal.ZERO;
			
			Calendar foy = (Calendar)begDate.clone();
			foy.set(Calendar.MONTH, 0);
			foy.set(Calendar.DATE, 1);

			//System.out.println("IncomeStatementPath: foy = "+sdf.format(foy.getTime()));
			//System.out.println("IncomeStatementPath: curr_bal = "+curr_bal.toString());
			
			for (GLTrans txn : acct.getTransactions()) {
				//System.out.println(txn.toString());
				if (txn.getDate() == null || txn.getAmount()==null)
					continue;
				if (txn.getDate().before(foy)) {
					//System.out.println("\ttxn comes before foy");
					continue;
				}
				if (ColeUtils.SameDay(txn.getDate(), begDate)) {
					//System.out.println("\ttxn same as begDate");
					continue;
				}
				if (txn.getDate().after(begDate)) {
					//System.out.println("\ttxn comes after the begDate");
					continue;
				}
				
				//System.out.println("\tadding to curr_bal");
				curr_bal = curr_bal.add(txn.getAmount());
			}
			
		} else {
			//BalanceSheetPath
			Calendar now = Calendar.getInstance();
			//System.out.println("BalanceSheetPath: now = "+sdf.format(now.getTime()));
			//System.out.println("BalanceSheetPath: curr_bal = "+curr_bal.toString());
			
			for (GLTrans txn : acct.getTransactions()) {
				//System.out.println(txn.toString());
				if (txn.getDate() == null || txn.getAmount()==null)
					continue;
				if (txn.getDate().before(begDate)) {
					//System.out.println("\ttxn comes before begDate");
					continue;
				}
				if (ColeUtils.SameDay(txn.getDate(), begDate)) {
					//System.out.println("\ttxn same as begDate");
					continue;
				}

				//System.out.println("\tsubtracting from curr_bal");
				curr_bal = curr_bal.subtract(txn.getAmount());
			}
			
		}

		//System.out.println("returning curr_bal = "+curr_bal.toString());
		return curr_bal;
	}

	public static List<GLTrans> GetAllTransactions(GLAccount acct, Calendar beginCal, Calendar endCal) {
		List<GLTrans> txns = new ArrayList<GLTrans>();
		
		//System.out.println("GetAllTransactions("+acct.getAcctNum()+") -> "+acct.getTransactions().size()+" txns");
		
		for (GLTrans txn : acct.getTransactions()) {
			Calendar txn_cal = txn.getDate();
			if (txn_cal == null)
				continue;
			if (beginCal.after(txn_cal))
				continue;
			if (endCal.before(txn_cal))
				continue;
			txns.add(txn);
		}
		
		return txns;
	}
}
