package com.intuit.gl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.intuit.ds.qb.CompanyMetaData;
import com.intuit.ds.qb.QBAccount;
import com.intuit.gl.data.GLAccount;
import com.intuit.gl.data.GLCompany;
import com.intuit.gl.data.GLTrans;
import com.intuit.query.QueryManager;

public class GatherGL {
	private static final String _FILENAME_ = "gl_company.ser";
	
	public static GLCompany fromDisk() throws Exception {
		File f = new File(_FILENAME_);
		if (!f.exists())
			throw new Exception("Serialized General Ledger not found");
		
		FileInputStream fileIn = new FileInputStream(_FILENAME_);
		ObjectInputStream in = new ObjectInputStream(fileIn);
        GLCompany comp = (GLCompany)in.readObject();
        in.close();
        fileIn.close();

        return comp;
	}
	
	public static GLCompany fromDB(QueryManager qm) throws Exception {
		GLCompany comp = new GLCompany();
		
		// first, get company stats
		List<CompanyMetaData> comp_mdata = qm.QueryCompanies();
		if (comp_mdata.size() != 1)
			throw new Exception("fromDB: QueryCompanies returned " + comp_mdata.size() + " companies ??");
		comp.setMetadata(comp_mdata.get(0));
		if (comp.getFyStart() == null) {
			
			if (comp.getQbnName().indexOf("LGL Administrative Services") >= 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
				Calendar fy = Calendar.getInstance();
				fy.setTime(sdf.parse("01-01"));
				comp.setFyStart(fy);
			}
			
			// TODO: fill this with any other companies
			
		}
		
		// next, let's fill in the accounts
		List<QBAccount> qb_accts = qm.QueryAccounts();
		List<GLAccount> gl_accts = comp.getAccounts();
		for (QBAccount qb_acct : qb_accts)
			gl_accts.add(new GLAccount(qb_acct));
		
		// ok, finally we need to pull out the transactions
		List<GLTrans> txns = qm.QueryTransactions();
		for (GLTrans txn : txns) {
			for (GLAccount acct : gl_accts) {
				if (txn.getAccountID()!=null && txn.getAccountID().equals(acct.getID())) {
					acct.getTransactions().add(txn);
					break;
				}
			}
		}
		
		return comp;
	}
	
	public static void pushToDisk(GLCompany comp) throws Exception {
		FileOutputStream fileOut = new FileOutputStream(_FILENAME_);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(comp);
		out.close();
		fileOut.close();
	}

}
