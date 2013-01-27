package com.intuit.query;

import java.util.*;
import com.intuit.ds.qb.*;
import com.intuit.data.*;
import com.intuit.platform.client.PlatformSessionContext;

public class QueryManager {
	private PlatformSessionContext _context;

	public QueryManager(PlatformSessionContext context) {
		_context = context;
	}
	
	public List<CompanyMetaData> QueryCompanies() throws Exception {

		QBCompanyService db = QBServiceFactory.getService(_context,
				QBCompanyService.class);
		CompaniesMetaData mdatas = db.getAvailableCompanies(_context);
		List<CompanyMetaData> mdata = mdatas.getCompanyMetaData();
		
		System.out.println("QueryCompanies: returned " + mdata.size() + " companies");
		return mdata;
	}

	public List<QBAccount> QueryAccounts() throws Exception {

		QBAccountService db = QBServiceFactory.getService(_context, QBAccountService.class);

		List<QBAccount> accounts = new ArrayList<QBAccount>();
		int page = 1, maxpages = 100;
		while (true) {
			List<QBAccount> list = db.findAll(_context, page, maxpages);
			if (list == null)
				break;
			System.out.println("QueryAccounts() -> found "+list.size());
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

		System.out.println("QueryAccounts: returned " + accounts.size() + " accounts");
		return accounts;
	}
	
	public QBAccount GetAccount(String acctnum) throws Exception {
		List<QBAccount> accounts = QueryAccounts();
		for (QBAccount acct : accounts) {
			if (acct.getAcctNum() != null) {
				if (acct.getAcctNum().equals(acctnum.trim())) {
					System.out.println("GetAccount("+acctnum+"):  -> "+acct.getName());
					return acct;
				}
			}
		}
		return null;
	}
	
	public List<GLTrans> QueryTransactions() throws Exception {
		List<GLTrans> txns = new ArrayList<GLTrans>();
		int page, maxpages = 100;

		// start with the Bills
		QBBillService billdb = QBServiceFactory.getService(_context, QBBillService.class);
		List<QBBill> bills = new ArrayList<QBBill>();
		page = 1;
		while (true) {
			List<QBBill> list = billdb.findAll(_context, page, maxpages);
			if (list == null)
				break;
			System.out.println("QueryTransactions: Bills: found "+list.size());
			bills.addAll(list);
			if (list.size() < maxpages)
				break;
			page++;
		}
		System.out.println("QueryTransactions: Bills: found " + bills.size() + " total Bills");
		
		for (QBBill bill : bills) {
			BillHeader head = bill.getHeader();
			for (BillLine line : bill.getLine()) {
				GLTrans txn = new GLTrans();
				txn.setAccountID(line.getAccountId());
				txn.setAmount(line.getAmount());
				txn.setDate(head.getTxnDate());
				txn.setDescription(line.getDesc());
				txn.setSource("Bill");
				txns.add(txn);
			}
		}

		// now look at bill payments
		QBBillPaymentService bpdb = QBServiceFactory.getService(_context, QBBillPaymentService.class);
		List<QBBillPayment> payments = new ArrayList<QBBillPayment>();
		page = 1;
		while (true) {
			List<QBBillPayment> list = bpdb.findAll(_context, page, maxpages);
			if (list == null)
				break;
			System.out.println("QueryTransactions: BillPayment: found "+list.size());
			payments.addAll(list);
			if (list.size() < maxpages)
				break;
			page++;
		}
		System.out.println("QueryTransactions: BillPayment: found " + payments.size() + " total Bills");
		
		for (QBBillPayment payment : payments) {
			BillPaymentHeader head = payment.getHeader();
			for (BillPaymentLine line : payment.getLine()) {
				GLTrans txn = new GLTrans();
				txn.setAccountID(null);
				txn.setAmount(line.getAmount());
				txn.setDate(head.getTxnDate());
				txn.setDescription(line.getDesc());
				txn.setSource("BillPayment");
				txns.add(txn);
			}
		}
		
		// TODO: add any more DB queries here...
		
		return txns;
	}
}
