<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils,
				com.intuit.query.QueryManager,
				com.intuit.ds.qb.QBAccount,
				java.util.List" %>
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
<p><a href="intuit_login.jsp">Go Back</a></p>
<ul>

 <% 
	try {
		String accesstoken = (String)session.getAttribute("accessToken");
		String accessstokensecret = (String)session.getAttribute("accessTokenSecret");
		String realmID = (String)session.getAttribute("realmId");
		String dataSource = (String)session.getAttribute("dataSource");
		PlatformSessionContext context = webutils.getPlatformContext(accesstoken,accessstokensecret,realmID,dataSource);

		// make query through Java code
		QueryManager qm = new QueryManager(context);
		List<QBAccount> accounts = qm.QueryAccounts();

		if (accounts == null || accounts.isEmpty()) {
			out.println("<p><b>ERROR: No accounts returned </b></p>");
		} else {
			// now ready to display the result
			out.print("<table>");
			out.println("<col width='50'><col width='350'><col width='150'>");
			out.print("<tr><th>Acct#</th><th>Name</th><th>Curr Bal</th></tr>");

			for (QBAccount acct : accounts) {
				out.print("<tr>");
				out.print("<td align='left'>" + acct.getAcctNum() + "</td>");
				out.print("<td align='center'>" + acct.getName() + "</td>");
				out.print("<td align='right'>" + acct.getCurrentBalance() + "</td>");
				out.println("</tr>");	
			}
			out.println("</table>");
		}
	
	} catch (Exception e) {		
		out.println("<p>Error: Servlet Exception thrown.</p>");
		System.out.println("Servlet Exception thrown: " + e.getMessage());
	}
 %>
</ul>
</body>
</html>
