<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils,
				com.intuit.utils.ColeUtils,
				com.intuit.query.QueryManager, 
				com.intuit.data.GLAccount,
				com.intuit.cole.ColeAccounting,
				java.util.List,
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
<title>Account Balances</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Account Balances</h3>
<p><a href="index.jsp">Return to Main Page</a></p>
<form action="accountbalances.jsp">
Begin Date: <input type="text" name="BegDate" value="mm-dd-yy">
End Date: <input type="text" name="EndDate" value="mm-dd-yy">
<input type="submit" value="Submit">
</form>
<ul>

 <% 
	try {
		String beg = request.getParameter("BegDate");
		String end = request.getParameter("EndDate");
		if (beg != null && end != null) {

			out.println("<p><b>Begin Date: " + beg + ", End Date: " + end + "</b></p>");

			String accesstoken = (String)session.getAttribute("accessToken");
			String accessstokensecret = (String)session.getAttribute("accessTokenSecret");
			String realmID = (String)session.getAttribute("realmId");
			String dataSource = (String)session.getAttribute("dataSource");
			PlatformSessionContext context = webutils.getPlatformContext(accesstoken,accessstokensecret,realmID,dataSource);

			boolean good = true;
			QueryManager qm = new QueryManager(context);
			Calendar beginCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
				beginCal.setTime(sdf.parse(beg));
				endCal.setTime(sdf.parse(end));

				if (beginCal.after(endCal))
					throw new Exception("Begin Date must come before End Date");
				//if (ColeUtils.CoversFYStart(beginCal, endCal, webutils))
				//	throw new Exception("Dates cannot span Fiscal Year start");
			} catch (Exception ex) {
				out.print("<p><b>ERROR: "+ex.getMessage()+"</b></p>");
				good = false;
			}

			if (good) {
				out.println("<p><b>Gray broke this (for now)</b></p>");

				/*
				// make query through Java code
				List<GLAccount> gl_accts = ColeAccounting.AccountBalances(qm, beginCal, endCal);

				// now ready to display the result
				out.print("<table>");
				out.print("<col width='50'><col width='350'>");
				out.println("<col width='150'><col width='150'><col width='150'><col width='150'>");
				out.print("<tr><th>Acct#</th><th>Name</th>");
				out.println("<th>Begin Bal</th><th>Positives</th><th>Negatives</th><th>End Bal</th></tr>");
	
				for (GLAccount gl_acct : gl_accts) {
					out.print("<tr>");
					out.print("<td align='left'>" + gl_acct.getAcctNum() + "</td>");
					out.print("<td align='center'>" + gl_acct.getName() + "</td>");
					out.print("<td align='right'>" + gl_acct.getBeginBalance().toString() + "</td>");
					out.print("<td align='right'>" + gl_acct.getPositives().toString() + "</td>");
					out.print("<td align='right'>" + gl_acct.getNegatives().toString() + "</td>");
					out.print("<td align='right'>" + gl_acct.getEndBalance().toString() + "</td>");
					out.println("</tr>");	
				}
				out.println("</table>");
				*/
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
