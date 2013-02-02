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
	String beg = request.getParameter("BegDate");
	String end = request.getParameter("EndDate");
%>

<script src="<%= appcenter_url %>/Content/IA/intuit.ipp.anywhere.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Account Balances</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Account Balances</h3>
<p><a href="index.jsp">Return to Main Page</a></p>
<form action="accountbalances.jsp">
Begin Date: <input type="text" name="BegDate" value="<%= (beg==null ? "mm-dd-yy" : beg) %>">
End Date: <input type="text" name="EndDate" value="<%= (end==null ? "mm-dd-yy" : end) %>">
<input type="submit" value="Submit">
</form>
<ul>

 <% 
 try {
		gl_file = getServletContext().getRealPath(gl_file);
		GLCompany comp = GatherGL.fromDisk(gl_file);

		if (beg != null && end != null) {
			boolean good = true;
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
			Calendar beginCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			try {
				// check valid dates
				beginCal.setTime(sdf.parse(beg));
				endCal.setTime(sdf.parse(end));
				if (beginCal.after(endCal))
					throw new Exception("Begin Date must come before End Date");
				if (ColeUtils.CoversFYStart(beginCal, endCal, comp.getFyStart()))
					throw new Exception("Dates cannot span Fiscal Year start");
			} catch (Exception ex) {
				out.print("<p><b>ERROR: "+ex.getMessage()+"</b></p>");
				good = false;
			} 

			if (good) {
				out.println("<p><b>Begin Date: " + beg + ", End Date: " + end + "</b></p>");

				// make query through Java code
				List<GLAccount> gl_accts = comp.getAccounts();

				// now ready to display the result
				out.print("<table>");
				out.print("<col width='50'><col width='350'>");
				out.println("<col width='150'><col width='150'><col width='150'><col width='150'>");
				out.print("<tr><th>Acct#</th><th>Name</th>");
				out.println("<th>Begin Bal</th><th>Positives</th><th>Negatives</th><th>End Bal</th></tr>");
	
				for (GLAccount gl_acct : gl_accts) {
					BigDecimal beg_bal = ProcessGL.GetBalanceAtDate( gl_acct, beginCal );
					List<GLTrans> txns = ProcessGL.GetAllTransactions(gl_acct, beginCal, endCal);
					BigDecimal pos = BigDecimal.ZERO;
					BigDecimal neg = BigDecimal.ZERO;
					BigDecimal end_bal = beg_bal;
					for (GLTrans txn : txns) {
						BigDecimal amt = txn.getAmount();
						if (amt == null)
							continue;
						if (amt.compareTo(BigDecimal.ZERO) >= 0)
							pos = pos.add(amt);
						else
							neg = neg.add( amt.abs() );
						end_bal = end_bal.add(amt);
					}

					out.print("<tr>");

					out.print("<td align='left'>");
					out.print("<a href=\"transactions.jsp?");
					out.print("AcctNum="+gl_acct.getAcctNum());
					out.print("&BegDate="+beg);
					out.print("&EndDate="+end+"\">");
					out.print(gl_acct.getAcctNum() + "</a>");
					out.print("</td>");
				
					out.print("<td align='center'>" + gl_acct.getName() + "</td>");
					out.print("<td align='right'>" + beg_bal.toString() + "</td>");
					out.print("<td align='right'>" + pos.toString() + "</td>");
					out.print("<td align='right'>" + neg.toString() + "</td>");
					out.print("<td align='right'>" + end_bal.toString() + "</td>");
					out.println("</tr>");	
				}
				out.println("</table>");
			}
		}
	} catch (Exception e) {		
		out.println("<p>Error: Exception thrown.</p>");
		System.out.println("Exception thrown: " + e.getMessage());
		e.printStackTrace();
	}
 %>
</ul>
</body>
</html>
