<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils,
				com.intuit.utils.ColeUtils,
				com.intuit.gl.data.GLTrans,
				com.intuit.gl.data.GLCompany,
				com.intuit.gl.data.GLAccount,
				com.intuit.gl.ProcessGL,
				com.intuit.gl.GatherGL,
				java.math.BigDecimal,
				java.util.Calendar,
				java.util.List,
				java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% 
	WebUtils webutils = new WebUtils(); 
	String app_url=webutils.getAppUrl(); 
	String appcenter_url=webutils.getAppcenterUrl();
	String gl_file = webutils.getGLSerializedFile();
	String acctnum = request.getParameter("AcctNum");
	String beg = request.getParameter("BegDate");
	String end = request.getParameter("EndDate");
%>

<script src="<%= appcenter_url %>/Content/IA/intuit.ipp.anywhere.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Account Transaction Analysis</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Account Transactions</h3>
<p><a href="index.jsp">Return to Main Page</a></p>
<%
if (beg!=null && end!=null) {
	out.print("<p><a href=\"accountbalances.jsp?");
	out.print("BegDate="+beg);
	out.print("&EndDate="+end);
	out.println("\">Return to Account Balances</a></p>");
}		
%>


<form action="transactions.jsp">
	Account Number: <input type="text" name="AcctNum" value="<%= (acctnum==null ? "" : acctnum) %>"><br>
	Begin Date: <input type="text" name="BegDate" value="<%= (beg==null ? "mm-dd-yy" : beg) %>">
	End Date: <input type="text" name="EndDate" value="<%= (end==null ? "mm-dd-yy" : end) %>">
	<input type="submit" value="Submit">
</form>
<ul>

<% 
	try {
		gl_file = getServletContext().getRealPath(gl_file);
		GLCompany comp = GatherGL.fromDisk(gl_file);

		if (acctnum != null && beg != null && end != null) {
			boolean good = true;
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
			Calendar beginCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			GLAccount acct = null;
			try {
				// check valid dates
				beginCal.setTime(sdf.parse(beg));
				endCal.setTime(sdf.parse(end));
				if (beginCal.after(endCal))
					throw new Exception("Begin Date must come before End Date");
				if (ColeUtils.CoversFYStart(beginCal, endCal, comp.getFyStart()))
					throw new Exception("Dates cannot span Fiscal Year start");

				// check valid account number
				if (acctnum.length() == 0)
					throw new Exception("Please enter Acct#");
				acct = ProcessGL.FindAccount(comp, acctnum);
				if (acct == null)
					throw new Exception("Acct# "+acctnum+" not found");
			} catch (Exception ex) {
				out.print("<p><b>ERROR: "+ex.getMessage()+"</b></p>");
				good = false;
			} 
		
			if (good) {
				out.println("<p><b>Account: " + acctnum + " ("+acct.getName()+")</b></p>");
				
				BigDecimal bal = ProcessGL.GetBalanceAtDate( acct, beginCal );
				out.println("<p><b>Begin Date: " + beg + ", Balance: $" + bal.toString() + "</b></p>");
				
				List<GLTrans> txns = ProcessGL.GetAllTransactions(acct, beginCal, endCal);
									
				// now ready to display the result
				out.print("<table>");
				out.println("<col width='100'><col width='250'><col width='150'><col width='150'>");
				out.println("<tr><th>Source</th><th>Description</th><th>Date</th><th>Amount</th></tr>");
	
				for (GLTrans txn : txns) {
					out.print("<tr>");
					out.print("<td align='left'>" + txn.getSource() + "</td>");
					out.print("<td align='center'>" + txn.getDescription() + "</td>");
					out.print("<td align='center'>" + sdf.format(txn.getDate().getTime()) + "</td>");
					if (txn.getAmount() != null) {
						out.print("<td align='right'>" + txn.getAmount().toString() + "</td>");
						bal = bal.add(txn.getAmount());
					} else {
						out.print("<td align='right'>null</td>");
					}
						
					out.println("</tr>");	
				}
				out.println("</table>");
			
				out.println("<p><b>End Date: " + end + ", Balance: $" + bal.toString() + "</b></p>");
			}
		}
	} catch (Exception e) {		
	out.println("<p>Error: Exception thrown: <b>"+e.getMessage()+"</b></p>");
		System.out.println("Exception thrown: " + e.getMessage());
	}
 %>
</ul>
</body>
</html>
