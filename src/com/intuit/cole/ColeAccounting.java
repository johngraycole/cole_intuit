package com.intuit.cole;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.intuit.query.QueryManager;
import com.intuit.data.GLTrans;
import com.intuit.data.GLAccount;
import com.intuit.ds.qb.*;

/*
import com.intuit.query.BillHeader;
import com.intuit.query.BillLine;
import com.intuit.query.BillPaymentHeader;
import com.intuit.query.BillPaymentLine;
import com.intuit.query.CompaniesMetaData;
import com.intuit.query.CompaniesMetaDataImpl;
import com.intuit.query.CompanyMetaData;
import com.intuit.query.QBAccount;
import com.intuit.query.QBBill;
import com.intuit.query.QBBillPayment;
import com.intuit.query.QBBillPaymentService;
import com.intuit.query.QBBillService;
*/

public class ColeAccounting {

	public static BigDecimal GetBalanceAtDate(QueryManager qm, QBAccount acct, Calendar reqDate) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, -now.get(Calendar.HOUR_OF_DAY));
		now.add(Calendar.MINUTE, -now.get(Calendar.MINUTE));
		now.add(Calendar.SECOND, -now.get(Calendar.SECOND));
		now.add(Calendar.MILLISECOND, -now.get(Calendar.MILLISECOND));
		now.add(Calendar.DATE, 1);

		// TODO: get all transactions between "reqDate" and "now"

		return BigDecimal.ZERO;
	}

	
	public static List<GLTrans> GetAllTransactions(QueryManager qm, QBAccount acct, Calendar begin, Calendar end) {
		List<GLTrans> txns = new ArrayList<GLTrans>();
		
		return txns;
	}
	
	public static List<GLAccount> AccountBalances(QueryManager qm, Calendar begDate, Calendar endDate) {
		List<GLAccount> accts = new ArrayList<GLAccount>();
		
		return accts;
	}
	
	public static void ScratchSpace(QueryManager qm, QBAccount acct) throws Exception {
		
		System.out.println("Gray broke this (for now)");
	}

	/*
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
		
		// test access to CompanyMetaData
      
		CompaniesMetaDataImpl mdata = new CompaniesMetaDataImpl(_context);
		CompaniesMetaData cosqbomd = mdata.getQboCompaniesMetaData();
		List<CompanyMetaData> cosqbomdList = cosqbomd.getCompanyMetaData();
		if (cosqbomdList.size() == 0) {
		    System.out.println(">>>");
		    System.out.println(">>> No LUCK getting Company Meta Data:");
		} else {
		    CompanyMetaData qbomd = cosqbomdList.get(0);
		    System.out.println(">>>");
		    System.out.println(">>> Company Meta Data:");
		    System.out.println("\tFiscal Year Start: "+qbomd.getFiscalYearStart());
		    System.out.println("\tTax Year Start: "+qbomd.getIncomeTaxYearStart());
		    System.out.println("\tTax ID: "+qbomd.getTaxIdentifier());
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		// start with the Bills
		QBBillService billdb = QBServiceFactory.getService(_context,
				QBBillService.class);
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		Date date = formatter.parse("01-01-2013");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		////// ORIGINALLY COMMENTED
		QBBillQuery billq = billdb.getQBObject(_context, QBBillQuery.class);
		billq.setChunkSize(100);
		billq.setStartPage(new BigInteger("1"));

		billq.setStartDueDate(cal);
		///////
		List<QBBill> bills = new ArrayList<QBBill>();
		int page = 1, pagemax = 100;
		System.out.println(">>>Querying Bills table...");
		while (true) {
			List<QBBill> list = billdb.findAll(_context, page, pagemax);
			System.out.println(">>>found "+list.size());
			for (int i=0; i<list.size(); i++) {
			    QBBill test = list.get(i);
			    BillHeader thead = test.getHeader();
			    if (thead.getTxnDate().after(cal))
				bills.add(test);
			}
			    //bills.addAll(list);
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
			System.out.println(">>>Bill Header AP Acct ID: "+head.getAPAccountId().getValue());
			System.out.println(">>>Bill Hdr AP Acct Name: "+head.getAPAccountName());
			System.out.println(">>>Bill Header Amount: "+head.getTotalAmt().toString());
			String date_str = sdf.format(head.getTxnDate().getTime()).toString();
			System.out.println(">>>Bill Header Txn Date: "+date_str);
			//System.out.println(">>>Bill Header Txn Date: "+head.getTxnDate());
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
		
		// now look at bill payments
		QBBillPaymentService bpdb = QBServiceFactory.getService(_context,
						  QBBillPaymentService.class);
		List<QBBillPayment> payments = new ArrayList<QBBillPayment>();
		page = 1;
		System.out.println(">>>Querying Bill Payments table...");
		while (true) {
			List<QBBillPayment> plist = bpdb.findAll(_context, page, pagemax);
			System.out.println(">>>found "+plist.size());
			for (int i=0; i<plist.size(); i++) {
			    QBBillPayment bptest = plist.get(i);
			    BillPaymentHeader pthead = bptest.getHeader();
			    if (pthead.getTxnDate().after(cal))
				payments.add(bptest);
			}
			    //bills.addAll(list);
			if (plist.size() < pagemax)
				break;
			page++;
		}
		System.out.println(">>>Found "+payments.size()+" total records in Bill Payments");
		toexamine = Math.min(10, payments.size());
		System.out.println(">>>Examining first "+toexamine+" records...");
		for (int i=0; i<toexamine; i++) {
			QBBillPayment pmt = payments.get(i);
			List<BillPaymentLine> plines = pmt.getLine();
			BillPaymentHeader phead = pmt.getHeader();
			// find non null values
			if (phead.getAPAccountId().getValue() != null)
			    System.out.println(">>>BillPmtHdr APAcctId: "+phead.getAPAccountId().getValue());
			if (phead.getAPAccountName() != null)
			    System.out.println(">>>Bill Pmt Hdr AP Acct Name: "+phead.getAPAccountName());
			if (phead.getBankAccountId().getValue() != null)
			    System.out.println(">>>BillPmtHdr BankAcctId: "+phead.getBankAccountId().getValue());
			if (phead.getBankAccountName() != null)
			    System.out.println(">>>Bill Pmt Hdr Bank Acct Name: "+phead.getBankAccountName());
			System.out.println(">>>Bill Pmt Header Amount: "+phead.getTotalAmt().toString());
			if (phead.getCCAccountId().getValue() != null)
			    System.out.println(">>>BillPmtHdr CCAcctId: "+phead.getCCAccountId().getValue());
			if (phead.getCCAccountName() != null)
			    System.out.println(">>>Bill Pmt Hdr CC Acct Name: "+phead.getCCAccountName());
			if (phead.getEntityId().getValue() != null)
			    System.out.println(">>>BillPmtHdr EntityId: "+phead.getEntityId().getValue());
			if (phead.getEntityName() != null)
			  System.out.println(">>>Bill Pmt Hdr Entity Name: "+phead.getEntityName());
			if (phead.getEntityType() != null)
			  System.out.println(">>>Bill Pmt Hdr Entity Type: "+phead.getEntityType().value());
			//if (phead.getPayeeAddr() != null)
			// System.out.println(">>>Bill Pmt Hdr Payee City: "+phead.getPayeeAddr().getCity());
			System.out.println(">>>11 possible header fields all others are empty --------: ");
			String date_str = sdf.format(phead.getTxnDate().getTime()).toString();
			System.out.println(">>>Bill Pmt Header Txn Date: "+date_str);
			//System.out.println(">>>Bill Header Txn Date: "+head.getTxnDate());
			System.out.println(">>>BillHeaderLines --------: ");
			System.out.println(">>>Num Lines: "+plines.size());
			for (BillPaymentLine pline : plines) {
			    System.out.println("\tDescription: "+pline.getDesc());
				System.out.println("\tAmount: "+pline.getAmount().toString());
			}
		}
	}
	*/
}
