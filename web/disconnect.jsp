<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Disconnect QuickBooks Intuit</title>
</head>
<!--  Note: In a production app, the page at the Disconnect Landing URL should verify that the user is currently signed
 in with OpenID, similar to the procedure in VerifyOpenIDServlet.java. -->
<body>
<h3>Hello World for Intuit Anywhere: Disconnect from QuickBooks</h3>
<p>Disconnecting will invalidate your OAuth token, so it cannot be used for future calls to Data Services.
Are you sure you want to disconnect from QuickBooks?</p>
<p>Yes: <a href="DisconnectServlet">Disconnect from QuickBooks</a></p>
<p>No: <a href="index.jsp">Return to Main Page</a></p>
</body>
</html>
