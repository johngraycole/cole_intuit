package com.intuit.gl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.List;

import com.intuit.ds.qb.CompanyMetaData;
import com.intuit.ds.qb.QBAccount;
import com.intuit.gl.data.GLAccount;
import com.intuit.gl.data.GLCompany;
import com.intuit.gl.data.GLTrans;
import com.intuit.query.QueryManager;

public class GatherGL {
	
	public static GLCompany fromDisk(String filename) throws Exception {
		File f = new File(filename);
		if (!f.exists())
			throw new Exception("Serialized General Ledger not found");
		
		FileInputStream fileIn = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(fileIn);
        GLCompany comp = (GLCompany)in.readObject();
        in.close();
        fileIn.close();

        return comp;
	}
	
	public static GLCompany fromDB(QueryManager qm, String qbn, 
			String ap_acct, String ar_acct, String bank_acct, 
			Calendar fyStart) throws Exception {
		
		boolean found;
		GLCompany comp = new GLCompany();
		
		// first, get company name
		List<CompanyMetaData> comp_mdata = qm.QueryCompanies();
		found = false;
		for (CompanyMetaData cmd : comp_mdata) {
			if (cmd.getQBNRegisteredCompanyName() != null &&
					cmd.getQBNRegisteredCompanyName().equals(qbn)) {
				comp.setQbnName(qbn);
				found = true;
				break;
			}
		}
		if (!found)
			throw new Exception("GatherGL: unable to find QBN: "+qbn);
		
		// now, set fy start
		comp.setFyStart(fyStart);
		
		// next, let's fill in the accounts
		List<QBAccount> qb_accts = qm.QueryAccounts();
		List<GLAccount> gl_accts = comp.getAccounts();
		for (QBAccount qb_acct : qb_accts) {
			if (qb_acct.getAcctNum() == null) {
				System.out.println("Ignoring null acct num: "+qb_acct.getName());
				continue;
			}
			gl_accts.add(new GLAccount(qb_acct));
			if (qb_acct.getAcctNum().equals(ar_acct))
				comp.setDefARacctNum(ar_acct);
			if (qb_acct.getAcctNum().equals(ap_acct))
				comp.setDefAPacctNum(ap_acct);
			if (qb_acct.getAcctNum().equals(bank_acct))
				comp.setDefBankAcctNum(bank_acct);
		}
		if (comp.getDefAPacctNum() == null)
			throw new Exception("GatherGL: unable to find AP Acct: "+ap_acct);
		if (comp.getDefARacctNum() == null)
			throw new Exception("GatherGL: unable to find AR Acct: "+ar_acct);
		if (comp.getDefBankAcctNum() == null)
			throw new Exception("GatherGL: unable to find Def Bank Acct: "+bank_acct);
		
		// ok, finally we need to pull out the transactions
		List<GLTrans> txns = qm.QueryTransactions(comp);
		int numfound=0;
		for (GLTrans txn : txns) {
			for (GLAccount acct : gl_accts) {
				if (txn.getAccountID()!=null && txn.getAccountID().equals(acct.getID())) {
					acct.getTransactions().add(txn);
					numfound++;
					break;
				}
			}
		}
		System.out.println("Of "+ txns.size() + " GLTrans, found accounts for "+numfound);
		
		return comp;
	}
	
	public static void pushToDisk(GLCompany comp, String filename) throws Exception {
		FileOutputStream fileOut = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(comp);
		out.close();
		fileOut.close();
	}
	
	public static void deleteSnapshot(String filename) throws Exception {
		File f = new File(filename);
		if (!f.exists())
			throw new Exception("Serialized General Ledger not found");
		f.delete();
	}

}
