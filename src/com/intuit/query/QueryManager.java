package com.intuit.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.intuit.platform.client.PlatformSessionContext;
import com.intuit.result.AnalysisResult;
import com.intuit.result.AnalysisResult.ColumnJustify;
import com.intuit.ds.qb.*;

public class QueryManager {
	private PlatformSessionContext _context;
	
	public QueryManager(PlatformSessionContext context) {
		_context = context;
	}
	
	public AnalysisResult AccountBalances(String begDate, String endDate) {
		AnalysisResult result;
		try {
			System.out.println("AccountBalances("+begDate+","+endDate+")");
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(sdf.parse(begDate));
			System.out.println("AccountBalances: beginCal = "+beginCal.getTime());
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(sdf.parse(endDate));
			System.out.println("AccountBalances: endCal = "+endCal.getTime());
			
			QBAccountService db = QBServiceFactory.getService(_context, 
					QBAccountService.class);

			List<QBAccount> accounts = new ArrayList<QBAccount>();
			int page = 1, maxpages = 100;
			while (true) {
				List<QBAccount> list = db.findAll(_context, page, maxpages);
				System.out.println("AccountBalances: found " + list.size() + 
						" accounts on page " + page);
				accounts.addAll(list);
				if (list.size() < maxpages)
					break;
				page++;
			}
			
			Collections.sort(accounts,
				new Comparator<QBAccount> () {
					public int compare(QBAccount a1, QBAccount a2) {
						if (a1.getAcctNum() == null)
							return -1;
						return a1.getAcctNum().compareTo(a2.getAcctNum());
					}	
				}
			);
			
			result = new AnalysisResult(accounts.size(), 5);
			result.setErrorMsg("");

			result.setColWidth(0, 50);
			result.setColTitle(0, "Acct#");
			result.setColJustify(0, ColumnJustify.LEFT_JUSTIFY);
			result.setColWidth(1, 315);
			result.setColTitle(1, "Name");
			result.setColJustify(1, ColumnJustify.CENTER_JUSTIFY);
			result.setColWidth(2, 100);
			result.setColTitle(2, "Begin Bal");
			result.setColJustify(2, ColumnJustify.RIGHT_JUSTIFY);
			result.setColWidth(3, 100);
			result.setColTitle(3, "Change");
			result.setColJustify(3, ColumnJustify.RIGHT_JUSTIFY);
			result.setColWidth(4, 100);
			result.setColTitle(4, "End Bal");
			result.setColJustify(4, ColumnJustify.RIGHT_JUSTIFY);
			
			for (int i=0; i<accounts.size(); i++) {
				result.setVal(i, 0, accounts.get(i).getAcctNum());
				result.setVal(i, 1, accounts.get(i).getName());
				result.setVal(i, 2, "??");
				result.setVal(i, 3, "??");
				java.math.BigDecimal bal = accounts.get(i).getCurrentBalance();
				if (bal == null) 
				    result.setVal(i, 4, "??");
				else
				    result.setVal(i, 4, bal.toString());
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			ex.printStackTrace(System.out);
			result = new AnalysisResult(1,1);
			result.setErrorMsg(ex.getMessage());
		}
		
		return result;
	}
	
	public AnalysisResult MakeTestResult() {
		AnalysisResult result = new AnalysisResult(3,3);
		
		result.setErrorMsg("");

		result.setColWidth(0, 50);
		result.setColTitle(0, "Num");
		result.setColJustify(0, ColumnJustify.LEFT_JUSTIFY);
		result.setColWidth(1, 300);
		result.setColTitle(1, "Name");
		result.setColJustify(1, ColumnJustify.CENTER_JUSTIFY);
		result.setColWidth(2, 75);
		result.setColTitle(2, "Bal");
		result.setColJustify(2, ColumnJustify.RIGHT_JUSTIFY);

		result.setVal(0, 0, "001");
		result.setVal(0, 1, "Account001");
		result.setVal(0, 2, "$12.32");
		result.setVal(1, 0, "002");
		result.setVal(1, 1, "Account002");
		result.setVal(1, 2, "$20.32");
		result.setVal(2, 0, "002");
		result.setVal(2, 1, "Account002");
		result.setVal(2, 2, "$24.32");
		
		return result;
	}

	public AnalysisResult AllAccounts() {
		AnalysisResult result;
		try {
			System.out.println("AllAccounts()");
			
			QBAccountService db = QBServiceFactory.getService(_context, 
					QBAccountService.class);

			List<QBAccount> accounts = new ArrayList<QBAccount>();
			int page = 1, maxpages = 100;
			while (true) {
				List<QBAccount> list = db.findAll(_context, page, maxpages);
				System.out.println("AccountBalances: found " + list.size() + 
						" accounts on page " + page);
				accounts.addAll(list);
				if (list.size() < maxpages)
					break;
				page++;
			}
			
			Collections.sort(accounts,
				new Comparator<QBAccount> () {
					public int compare(QBAccount a1, QBAccount a2) {
						if (a1.getAcctNum() == null)
							return -1;
						return a1.getAcctNum().compareTo(a2.getAcctNum());
					}	
				}
			);
			
			result = new AnalysisResult(accounts.size(), 4);
			result.setErrorMsg("");

			result.setColWidth(0, 50);
			result.setColTitle(0, "Acct#");
			result.setColJustify(0, ColumnJustify.LEFT_JUSTIFY);
			result.setColWidth(1, 315);
			result.setColTitle(1, "Name");
			result.setColJustify(1, ColumnJustify.CENTER_JUSTIFY);
			result.setColWidth(2, 100);
			result.setColTitle(2, "Begin Date");
			result.setColJustify(2, ColumnJustify.CENTER_JUSTIFY);
			result.setColWidth(3, 100);
			result.setColTitle(3, "Begin Bal");
			result.setColJustify(3, ColumnJustify.RIGHT_JUSTIFY);
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
			for (int i=0; i<accounts.size(); i++) {
				
				result.setVal(i, 0, accounts.get(i).getAcctNum());
				
				result.setVal(i, 1, accounts.get(i).getName());
				
				Calendar beg_date = accounts.get(i).getOpeningBalanceDate();
				if (beg_date != null)
					result.setVal(i, 2, sdf.format(beg_date.getTime()).toString());
				else
					result.setVal(i, 2, "null");

				result.setVal(i, 3, "$" + accounts.get(i).getOpeningBalance());
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			ex.printStackTrace(System.out);
			result = new AnalysisResult(1,1);
			result.setErrorMsg(ex.getMessage());
		}
		
		return result;
	}
}
