<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils,
				com.intuit.query.QueryManager,
				com.intuit.gl.data.GLAccount,
				com.intuit.gl.data.GLCompany,
				com.intuit.gl.GatherGL" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Delete DB Snapshot</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Delete DB Snapshot</h3>
<p><a href="intuit_login.jsp">Go Back</a></p>
<ul>

<% 
    try {
        WebUtils webutils = new WebUtils(); 
	    String gl_file = webutils.getGLSerializedFile();
		gl_file = getServletContext().getRealPath(gl_file);
		
		GatherGL.deleteSnapshot(gl_file);
		out.println("<p>General Ledger DB deleted successfully:</p>");
	} catch (Exception e) {		
    out.println("<p>Error: <b>"+e.getMessage()+"</b></p>");
		System.out.println("Servlet Exception thrown: " + e.getMessage());
	}
%>
</ul>
</body>
</html>
