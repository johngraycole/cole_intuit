<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.utils.WebUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:ipp="">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cole Intuit Accounting Suite</title>
<% 
	WebUtils webutils = new WebUtils(); 
	String app_url=webutils.getAppUrl(); 
	String appcenter_url=webutils.getAppcenterUrl();
%>
<script type="text/javascript" src="<%= appcenter_url %>/Content/IA/intuit.ipp.anywhere.js"></script>

<script>intuit.ipp.anywhere.setup({
    menuProxy: '<%= app_url %>/Cole_Intuit/BlueDotMenuServlet',
    grantUrl: '<%= app_url %>/Cole_Intuit/RequestTokenServlet'
});</script>

</head>
<body>
<h3>Cole Intuit Accounting Suite: Main Page</h3>
<% 
	String connectionStatus = (String)session.getAttribute("connectionStatus");
 	if(connectionStatus!=null && connectionStatus.equalsIgnoreCase("authorized"))
	{ 
%>
		<p>To see the apps available to the user, select the Intuit "blue dot" menu.</p>
		<p>The OAuth flow has been completed. Because the app now has an authorized access token, 
			it can access QuickBooks data by making calls to Data Services.</p>
		<ul>
			<li><a href="accounts.jsp">Display All QuickBooks Accounts</a></li>
			<li><a href="accountbalances.jsp">Query Account Balances</a></li>
			<li><a href="transactions.jsp">Query Account Transactions</a></li>
			<li><a href="scratch_space.jsp">Scratch Space (for testing only)</a></li>
			<li><a href="disconnect.jsp">Disconnect from QuickBooks Intuit</a></li>
		</ul> 

 		<ipp:blueDot></ipp:blueDot>
<% 
	} else {
		session.setAttribute("flowType", "connect_button");
%>
		<p>To authorize this app to access your QuickBooks data, click the "Connect to QuickBooks" button.</p> 
		<p>This will start the three-legged OAuth flow.</p>
		<p><ipp:connectToIntuit></ipp:connectToIntuit></p>
<%
	}
%>
</body>
</html>
