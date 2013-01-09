<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.utils.WebUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:ipp="">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dashboard</title>
<% WebUtils webutils = new WebUtils(); String app_url=webutils.getAppUrl(); String appcenter_url=webutils.getAppcenterUrl();%>
<script type="text/javascript" src="<%= appcenter_url %>/Content/IA/intuit.ipp.anywhere.js"></script>

<script>intuit.ipp.anywhere.setup({
    menuProxy: '<%= app_url %>/Cole_Intuit/BlueDotMenuServlet',
    grantUrl: '<%= app_url %>/Cole_Intuit/RequestTokenServlet'
});</script>

</head>
<body>
<h3>Hello World for Intuit Anywhere: Dashboard</h3>
 <% String connectionStatus = (String)session.getAttribute("connectionStatus");
 if(connectionStatus!=null && connectionStatus.equalsIgnoreCase("authorized"))
 {%>
 <p>To see the apps available to the user, select the Intuit "blue dot" menu.</p>
<p>The OAuth flow has been completed. Because the app now has an authorized access token, 
it can access QuickBooks data by making calls to Data Services.</p>
<ul>
<li><a href="findallcustomers.jsp">Find All QuickBooks Customers</a></li>
<li><a href="test.jsp">Find All QuickBooks Accounts</a></li>
<li><a href="showtoken.jsp">Show Token Values</a></li>
<li><a href="disconnect.jsp">Disconnect from QuickBooks</a></li>
<li><a href="logout.jsp">Sign Out from Intuit App Center</a></li>
</ul> 

 <ipp:blueDot></ipp:blueDot>
 <%}else{
	 session.setAttribute("flowType", "connect_button");
  %>
<p>To authorize this app to access your QuickBooks data, click the "Connect to QuickBooks" button.  This will start the three-legged OAuth flow.</p>
<p>
<ipp:connectToIntuit></ipp:connectToIntuit>
</p>
 <%}%>
</body>
</html>
