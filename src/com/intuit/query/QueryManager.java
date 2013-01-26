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

	public List<QBAccount> QueryAccounts() throws Exception {

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
	
	public QBAccount GetAccount(String acctnum) throws Exception {

		List<QBAccount> accounts = QueryAccounts();
		for (QBAccount acct : accounts) {
			if (acct.getAcctNum() != null) {
				if (acct.getAcctNum().equals(acctnum.trim())) {
					System.out.println("GetAccount(): "+acctnum+" -> "+acct.getName());
					return acct;
				}
			}
		}
		return null;
	}
}
