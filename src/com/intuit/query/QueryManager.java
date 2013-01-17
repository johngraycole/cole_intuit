package com.intuit.query;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.intuit.ds.qb.*;
import com.intuit.platform.client.PlatformSessionContext;
import com.intuit.result.AnalysisResult;
import com.intuit.result.AnalysisResult.ColumnJustify;

public class QueryManager {
	private PlatformSessionContext _context;

	public QueryManager(PlatformSessionContext context) {
		_context = context;
	}

	public QBAccount GetAccount(String acctnum) throws Exception {
		List<QBAccount> accounts = getAllAccounts();
		for (QBAccount acct : accounts) {
			if (acct.getAcctNum() != null) {
				//System.out.println("Compare: <"+acct.getAcctNum() + "><"+acctnum+">");
				if (acct.getAcctNum().equals(acctnum.trim())) {
					System.out.println("GetAccount(): "+acctnum+" -> "+acct.getName());
					return acct;
				}
			}
		}
		return null;
	}

	public AnalysisResult AccountBalances(Calendar begDate, Calendar endDate) {
		AnalysisResult result;
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
			System.out.println("AccountBalances("
					+ sdf.format(begDate.getTime()) + ","
					+ sdf.format(endDate.getTime()) + ")");

			List<QBAccount> accounts = getAllAccounts();

			result = new AnalysisResult(accounts.size(), 6);
			result.setErrorMsg("");

			result.setColWidth(0, 50);
			result.setColTitle(0, "Acct#");
			result.setColJustify(0, ColumnJustify.LEFT_JUSTIFY);
			result.setColWidth(1, 350);
			result.setColTitle(1, "Name");
			result.setColJustify(1, ColumnJustify.CENTER_JUSTIFY);
			result.setColWidth(2, 150);
			result.setColTitle(2, "Begin Bal");
			result.setColJustify(2, ColumnJustify.RIGHT_JUSTIFY);
			result.setColWidth(3, 150);
			result.setColTitle(3, "Change");
			result.setColJustify(3, ColumnJustify.RIGHT_JUSTIFY);
			result.setColWidth(4, 150);
			result.setColTitle(4, "End Bal");
			result.setColJustify(4, ColumnJustify.RIGHT_JUSTIFY);

			for (int i = 0; i < accounts.size(); i++) {
				result.setVal(i, 0, accounts.get(i).getAcctNum());
				result.setVal(i, 1, accounts.get(i).getName());

				BigDecimal beg_bal = GetBalanceAtDate(accounts.get(i), begDate);
				BigDecimal end_bal = GetBalanceAtDate(accounts.get(i), endDate);

				if (beg_bal == null)
					result.setVal(i, 2, "??");
				else
					result.setVal(i, 2, "$" + beg_bal.toString());

				if (beg_bal == null || end_bal == null)
					result.setVal(i, 3, "??");
				else
					result.setVal(i, 3, "$"
							+ end_bal.subtract(beg_bal).toString());

				if (end_bal == null)
					result.setVal(i, 4, "??");
				else
					result.setVal(i, 4, "$" + end_bal.toString());
			}

		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			ex.printStackTrace(System.out);
			result = new AnalysisResult(1, 1);
			result.setErrorMsg(ex.getMessage());
		}

		return result;
	}

	public AnalysisResult AllAccounts() {
		AnalysisResult result;
		try {
			System.out.println("AllAccounts()");

			List<QBAccount> accounts = getAllAccounts();

			result = new AnalysisResult(accounts.size(), 3);
			result.setErrorMsg("");

			result.setColWidth(0, 50);
			result.setColTitle(0, "Acct#");
			result.setColJustify(0, ColumnJustify.LEFT_JUSTIFY);
			result.setColWidth(1, 350);
			result.setColTitle(1, "Name");
			result.setColJustify(1, ColumnJustify.CENTER_JUSTIFY);
			result.setColWidth(2, 150);
			result.setColTitle(2, "Curr Bal");
			result.setColJustify(2, ColumnJustify.RIGHT_JUSTIFY);

			for (int i = 0; i < accounts.size(); i++) {
				result.setVal(i, 0, accounts.get(i).getAcctNum());
				result.setVal(i, 1, accounts.get(i).getName());
				BigDecimal curr_bal = accounts.get(i).getCurrentBalance();
				if (curr_bal == null)
					result.setVal(i, 2, "??");
				else
					result.setVal(i, 2, "$" + curr_bal);
			}

		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			ex.printStackTrace(System.out);
			result = new AnalysisResult(1, 1);
			result.setErrorMsg(ex.getMessage());
		}

		return result;
	}

	public BigDecimal GetBalanceAtDate(QBAccount acct, Calendar reqDate) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, -now.get(Calendar.HOUR_OF_DAY));
		now.add(Calendar.MINUTE, -now.get(Calendar.MINUTE));
		now.add(Calendar.SECOND, -now.get(Calendar.SECOND));
		now.add(Calendar.MILLISECOND, -now.get(Calendar.MILLISECOND));
		now.add(Calendar.DATE, 1);

		// TODO: get all transactions between "reqDate" and "now"

		return null;
	}

	public AnalysisResult GetTransactions(QBAccount acct, Calendar begin,
			Calendar end) {
		// TODO: implement this function
		AnalysisResult result = new AnalysisResult(1, 1);
		result.setErrorMsg("GetTransactions not implemented");
		return result;
	}

	public void ScratchSpace(QBAccount acct) throws Exception {
		System.out.println(">>>");
		System.out.println(">>>");
		System.out.println(">>>");
		System.out.println(">>> Account:");
		System.out.println("\tNum: "+acct.getAcctNum());
		System.out.println("\tName: "+acct.getName());
		System.out.println("\tId: "+acct.getId().getValue());
		System.out.println("\tType: "+acct.getType());
		System.out.println("\tCurrBal: "+acct.getCurrentBalance().toString());
		
		// start with the Bills
		QBBillService billdb = QBServiceFactory.getService(_context,
				QBBillService.class);
		List<QBBill> bills = new ArrayList<QBBill>();
		int page = 1, pagemax = 100;
		System.out.println(">>>Querying Bills table...");
		while (true) {
			List<QBBill> list = billdb.findAll(_context, page, pagemax);
			System.out.println(">>>found "+list.size());
			bills.addAll(list);
			if (list.size() < pagemax)
				break;
			page++;
		}
		System.out.println(">>>Found "+bills.size()+" total records in Bills");
		int toexamine = Math.min(10, bills.size());
		System.out.println(">>>Examining first "+toexamine+" records...");
		
		for (int i=0; i<toexamine; i++) {
			QBBill bill = bills.get(i);
			List<BillLine> lines = bill.getLine();
			BillHeader head = bill.getHeader();
			System.out.println(">>>Bill Id: "+bill.getId().getValue());
			System.out.println(">>>Bill Header: "+head.getAPAccountId().getValue());
			System.out.println(">>>Bill Header Amount: "+head.getTotalAmt().toString());
			System.out.println(">>>Bill Header Txn Date: "+head.getTxnDate());
			System.out.println(">>>BillLines --------: ");
			System.out.println(">>>Num Lines: "+lines.size());
			for (BillLine line : lines) {
				System.out.println("\tAcctName: "+line.getAccountName());
				System.out.println("\tAcctId: "+line.getAccountId().getValue());
				System.out.println("\tAmount: "+line.getAmount().toString());
			}
		}
		
		System.out.println(">>>");
		System.out.println(">>>");
		System.out.println(">>>");
	}

	private List<QBAccount> getAllAccounts() throws Exception {

		QBAccountService db = QBServiceFactory.getService(_context,
				QBAccountService.class);

		List<QBAccount> accounts = new ArrayList<QBAccount>();
		int page = 1, maxpages = 100;
		while (true) {
			List<QBAccount> list = db.findAll(_context, page, maxpages);
			if (list == null)
				break;
			System.out.println("getAllAccounts() -> found "+list.size());
			accounts.addAll(list);
			if (list.size() < maxpages)
				break;
			page++;
		}
		
		Collections.sort(accounts, new Comparator<QBAccount>() {
			public int compare(QBAccount a1, QBAccount a2) {
				if (a1.getAcctNum() == null)
					return (a2.getAcctNum()==null ? 0 : -1);
				if (a2.getAcctNum() == null)
					return 1;
				return a1.getAcctNum().compareTo(a2.getAcctNum());
			}
		});

		System.out.println("getAllAccounts: query returned " + accounts.size()
				+ " accounts");
		return accounts;
	}
}
