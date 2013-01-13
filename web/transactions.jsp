<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils,
				com.intuit.query.QueryManager, 
				com.intuit.result.AnalysisResult,
				com.intuit.result.AnalysisResult.ColumnJustify,
				com.intuit.ds.qb.QBAccount,
				java.math.BigDecimal,
				java.util.Calendar,
				java.text.SimpleDateFormat" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% 
	WebUtils webutils = new WebUtils(); 
	String app_url=webutils.getAppUrl(); 
	String appcenter_url=webutils.getAppcenterUrl();
%>

<script src="<%= appcenter_url %>/Content/IA/intuit.ipp.anywhere.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Account Transaction Analysis</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Account Transactions</h3>
<p><a href="index.jsp">Return to Main Page</a></p>
<form action="transactions.jsp">
	Account Number: <input type="text" name="AcctNum" value=""><br>
	Begin Date: <input type="text" name="BegDate" value="mm-dd-yy">
	End Date: <input type="text" name="EndDate" value="mm-dd-yy">
	<input type="submit" value="Submit">
</form>
<ul>

<% 
	try {
		String acctnum = request.getParameter("AcctNum");
		String beg = request.getParameter("BegDate");
		String end = request.getParameter("EndDate");
		if (acctnum != null && beg != null && end != null) {

			String accesstoken = (String)session.getAttribute("accessToken");
			String accessstokensecret = (String)session.getAttribute("accessTokenSecret");
			String realmID = (String)session.getAttribute("realmId");
			String dataSource = (String)session.getAttribute("dataSource");
			PlatformSessionContext context = webutils.getPlatformContext(accesstoken,accessstokensecret,realmID,dataSource);
			boolean good = true;
			QueryManager qm = new QueryManager(context);
			QBAccount acct = null;
			Calendar beginCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			try {
				acct = qm.GetAccount(acctnum);
				if (acct == null)
					throw new Exception("Acct# "+acctnum+" not found");
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
				beginCal.setTime(sdf.parse(beg));
				endCal.setTime(sdf.parse(end));

				Calendar acct_beg = acct.getOpeningBalanceDate();
				if (acct_beg == null)
					throw new Exception("Acct# "+acctnum+" has null Begin Bal Date");
				if (beginCal.after(endCal))
					throw new Exception("Begin Date must come before End Date");
			} catch (Exception ex) {
				out.print("<p><b>ERROR: "+ex.getMessage()+"</b></p>");
				good = false;
			} 
		
			if (good) {
				out.println("<p><b>Account: " + acctnum + " ("+acct.getName()+")</b></p>");
				
				BigDecimal beg_bal = qm.GetBalanceAtDate( acct, beginCal );
				out.println("<p><b>Begin Date: " + beg + ", Balance: $" + beg_bal.toString() + "</b></p>");
				
				AnalysisResult result = qm.GetTransactions(acct, beginCal, endCal);
				String errmsg = result.getErrorMsg();
				if (errmsg != null && errmsg.length() > 0) {
					out.println("<p><b>ERROR: " + errmsg + "</b></p>");
				} else {
					// now ready to display the result
					out.print("<table>");
					for (int i=0; i<result.getNumCols(); i++)
						out.print("<col width='" + result.getColWidth(i) + "'>");
					out.println();
					out.print("<tr>");
					for (int i=0; i<result.getNumCols(); i++)
						out.print("<th>" + result.getColTitle(i) + "</th>");
					out.println("</tr>");
		
					for (int row=0; row<result.getNumRows(); row++) {
						out.print("<tr>");
						for (int col=0; col<result.getNumCols(); col++) {
							out.print("<td align='");
							switch (result.getColJustify(col)) {
								case CENTER_JUSTIFY:
									out.print("center");
									break;
								case RIGHT_JUSTIFY:
									out.print("right");
									break;
								case LEFT_JUSTIFY:
								default:
									out.print("center");
									break;
							}
							out.print("'>" + result.getVal(row,col) + "</td>");
						}
						out.println("</tr>");
					}
					out.println("</table>");
				}
			
				BigDecimal end_bal = qm.GetBalanceAtDate( acct, endCal );
				out.println("<p><b>End Date: " + end + ", Balance: $" + end_bal.toString() + "</b></p>");
			}
		}
	} catch (Exception e) {		
		out.println("<p>Error: Exception thrown.</p>");
		System.out.println("Exception thrown: " + e.getMessage());
	}
 %>
</ul>
</body>
</html>
