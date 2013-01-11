<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List, java.util.Iterator, 
				com.intuit.ds.qb.QBAccount, com.intuit.ds.qb.QBAccountService, 
				com.intuit.ds.qb.QBInvalidContextException, com.intuit.ds.qb.QBServiceFactory, 
				com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils" %>
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
<title>Display All Accounts</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Accounts</h3>
<p><a href="index.jsp">Return to Main Page</a></p>
<ul>

 <% 
	String accesstoken = (String)session.getAttribute("accessToken");
	String accessstokensecret = (String)session.getAttribute("accessTokenSecret");
	String realmID = (String)session.getAttribute("realmId");
	String dataSource = (String)session.getAttribute("dataSource");
					
	PlatformSessionContext context = webutils.getPlatformContext(accesstoken,accessstokensecret,realmID,dataSource);

	if (context == null) {
		out.println("<p>Error: PlatformSessionContext is null.</p>");
	}
	QBAccountService accountService = null;
	try {
		// Create the customer service.
		accountService = QBServiceFactory.getService(context,
				QBAccountService.class);
	} catch (QBInvalidContextException e) {
		System.out.println("(QBInvalidContextException thrown by getService: " + e.getMessage());
	}

	out.println("<table><col width='50'><col width='300'><col width='75'><col width='75'>");
	out.println("<tr><th>Number</th><th>Name</th><th>BeginBal</th><th>CurrentBal</th></tr>");
	try {
		// Using the service, retrieve all customers and display their names.
		List<QBAccount> accounts = accountService
				.findAll(context,1,100);
		Iterator itr = accounts.iterator();
		while (itr.hasNext()) {
			QBAccount account = (QBAccount) itr.next();
			String accountName = account.getName();
			String accountNum = account.getAcctNum();
			java.math.BigDecimal bal = account.getCurrentBalance();
			java.math.BigDecimal bbal = account.getOpeningBalance();
			out.println("<tr>");
			out.println("<td>" + accountNum + "</td><td>" + accountName + "</td>");
			if (bbal == null)
				out.println("<td>??</td>");
			else		   
				out.println("<td align='right'>" + bbal.toString() + "</td>");
			if (bal == null) 
			   out.println("<td>??</td>");
			else
				out.println("<td align='right'>" + bal.toString() + "</td>");
			out.println("</tr>");
		} // while
	} catch (Exception e) {
		System.out.println("Exception thrown by findAll: " + e.getMessage());
	}
 %>
</table>
</ul>
</body>
</html>
