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
<% 
	WebUtils webutils = new WebUtils(); 
	String app_url=webutils.getAppUrl(); 
	String appcenter_url=webutils.getAppcenterUrl();
	String gl_file = webutils.getGLSerializedFile();
%>

<script src="<%= appcenter_url %>/Content/IA/intuit.ipp.anywhere.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Refresh DB Snapshot</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Refresh DB Snapshot</h3>
<p><a href="index.jsp">Return to Main Page</a></p>
<ul>

 <% 
	try {
		String accesstoken = (String)session.getAttribute("accessToken");
		String accessstokensecret = (String)session.getAttribute("accessTokenSecret");
		String realmID = (String)session.getAttribute("realmId");
		String dataSource = (String)session.getAttribute("dataSource");
		PlatformSessionContext context = webutils.getPlatformContext(accesstoken,accessstokensecret,realmID,dataSource);
		gl_file = getServletContext().getRealPath(gl_file);
		
		QueryManager qm = new QueryManager(context);
		GLCompany comp = GatherGL.fromDB(qm);
		GatherGL.pushToDisk(comp, gl_file);
		System.out.println("Wrote GL Serialized File: "+gl_file);

		out.println("<p>General Ledger DB queried successfully:</p>");
		out.println("<p>Company Name: "+comp.getQbnName()+"</p>");
		out.println("<p>Accounts</p>");
		out.println("<ul>");
		for (GLAccount acct : comp.getAccounts()) {
			out.print("<li>"+acct.getAcctNum() + " " + acct.getName());
			out.println(" -> "+acct.getTransactions().size()+" transactions</li>");
		}
		out.println("</ul>"); 
		
	} catch (Exception e) {		
		out.println("<p>Error: Servlet Exception thrown.</p>");
		System.out.println("Servlet Exception thrown: " + e.getMessage());
	}
 %>
</ul>
</body>
</html>
