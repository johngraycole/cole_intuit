<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils,
				com.intuit.query.QueryManager, 
				com.intuit.ds.qb.QBAccount" %>
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
<title>Scratch Workspace</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Scratch Space</h3>
<p><a href="index.jsp">Return to Main Page</a></p>
<form action="scratch_space.jsp">
	Account Number: <input type="text" name="AcctNum" value="">
	<input type="submit" value="Submit">
</form>
<ul>

<% 
	try {
		String acctnum = request.getParameter("AcctNum");
		if (acctnum != null) {

			String accesstoken = (String)session.getAttribute("accessToken");
			String accessstokensecret = (String)session.getAttribute("accessTokenSecret");
			String realmID = (String)session.getAttribute("realmId");
			String dataSource = (String)session.getAttribute("dataSource");
			PlatformSessionContext context = webutils.getPlatformContext(accesstoken,accessstokensecret,realmID,dataSource);
			QueryManager qm = new QueryManager(context);
			try {
				QBAccount acct = qm.GetAccount(acctnum);
				if (acct == null)
					throw new Exception("Acct# "+acctnum+" not found");
				qm.ScratchSpace(acct);
				out.println("<p>Were you watching?</p>");
			} catch (Exception ex) {
				out.print("<p><b>ERROR: "+ex.getMessage()+"</b></p>");
			}
		} else {
			out.println("<p>Tail my catalina.out, then enter Acct# and click Submit</p>");
		}
	} catch (Exception e) {		
		out.println("<p>Error: Exception thrown.</p>");
		System.out.println("Exception thrown: " + e.getMessage());
	}
 %>
</ul>
</body>
</html>
