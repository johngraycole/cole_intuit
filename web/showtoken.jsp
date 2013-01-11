<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.utils.WebUtils;" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Show Tokens</title>
</head>
<body>
<% 
	WebUtils prop = new WebUtils();
%>

<p>OAuth Values:</p>
<ul>
<li>Consumer Key: <%= prop.getConsumerKey() %></li>
<li>Consumer Secret: <%= prop.getConsumerSecret() %></li>
<li>Access Token: <%= (String)session.getAttribute("accessToken") %></li>
<li>Access Token Secret: <%= (String)session.getAttribute("accessTokenSecret") %></li>
</ul>

<p>OpenID Values (These are null if the user is not validated with OpenID.):</p>
<ul>
<li>OpenID Identity: <%= (String)session.getAttribute("openIDidentity") %></li>
<li>First Name: <%= (String)session.getAttribute("firstName") %></li>
<li>Last Name: <%= (String)session.getAttribute("lastName") %></li>
<li>Email: <%= (String)session.getAttribute("email") %></li>

</ul>
<p>Other Values:</p>
<ul>
<li>App Token: <%= prop.getappToken() %></li>
<li>Realm ID: <%= (String)session.getAttribute("realmId") %></li>
<li>Data Source: <%= (String)session.getAttribute("dataSource") %></li>
</ul>
 
<p>Note: Do not display the tokens, keys, and secrets in a production environment.</p>

<p><a href="index.jsp">Go to Main Page</a></p>
</body>
</html>
