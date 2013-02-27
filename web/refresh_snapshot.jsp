<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.intuit.platform.client.PlatformSessionContext, 
				com.intuit.utils.WebUtils,
                java.text.SimpleDateFormat,
                java.util.Calendar,
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
    gl_file = getServletContext().getRealPath(gl_file);
    GLCompany comp = null;
    try {
        comp = GatherGL.fromDisk(gl_file);
    } catch (Exception ex) {
        System.out.println("GL Serialized file not found: " + ex.getMessage());
    }
    
	String qbn = request.getParameter("QBN"); 
    String ap_acct = request.getParameter("APAcct");
	String ar_acct = request.getParameter("ARAcct");
	String bank_acct = request.getParameter("DefBankAcct");
    String fy_start = request.getParameter("FYStart");
    if (comp != null) {
        if (qbn==null)
            qbn = comp.getQbnName();
        if (ap_acct==null)
            ap_acct = comp.getDefAPacctNum();
        if (ar_acct==null)
            ar_acct = comp.getDefARacctNum();
        if (bank_acct == null)
            bank_acct = comp.getDefBankAcctNum();
        if (fy_start == null)
            fy_start = comp.getFyStartString("MM-dd");
    }
%>

<script src="<%= appcenter_url %>/Content/IA/intuit.ipp.anywhere.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Refresh DB Snapshot</title>
</head>
<body>
<h3>Cole Intuit Accounting Suite: Refresh DB Snapshot</h3>
<p><a href="intuit_login.jsp">Go Back</a></p>
<form action="refresh_snapshot.jsp">
<table>
    <tr><td><label for="txtCompName">Company Name:</label></td>
        <td><input type="text" name="QBN" value="<%= (qbn==null ? "" : qbn) %>"></td></tr>
    
    <tr><td><label for="txtDefAPAcct">Def AP Acct:</label></td>
        <td><input type="text" name="APAcct" value="<%= (ap_acct==null ? "" : ap_acct) %>"></td></tr>
    
    <tr><td><label for="txtDefARAcct">Def AR Acct:</label></td>
        <td><input type="text" name="ARAcct" value="<%= (ar_acct==null ? "" : ar_acct) %>"></td></tr>

    <tr><td><label for="txtDefBankAcct">Def Bank Acct:</label></td>
        <td><input type="text" name="DefBankAcct" value="<%= (bank_acct==null ? "" : bank_acct) %>"></td></tr>

    <tr><td><label for="txtFyStart">FY Start:</label></td>
        <td><input type="text" name="FYStart" value="<%= (fy_start==null ? "mm-dd" : fy_start) %>"></td></tr>
</table>
	<input type="submit" value="Submit">
</form>

 <% 
	try {
        if (qbn!=null && ap_acct!=null && ar_acct!=null && bank_acct!=null && fy_start!=null) {
            System.out.println("non-null params, here we go...");
			boolean good = true;
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			Calendar fyCal = Calendar.getInstance();
			try {
				// check valid dates
				fyCal.setTime(sdf.parse(fy_start));

				if (qbn.length() == 0)
					throw new Exception("Please enter Company QBN");
                if (ap_acct.length() == 0)
					throw new Exception("Please enter AP Acct#");
				if (ar_acct.length() == 0)
					throw new Exception("Please enter AR Acct#");
                if (bank_acct.length() == 0)
					throw new Exception("Please enter Def Bank Acct#");
			} catch (Exception ex) {
                System.out.println("ERROR: "+ex.getMessage());
				out.print("<p><b>ERROR: "+ex.getMessage()+"</b></p>");
				good = false;
			} 

            if (good) {
                System.out.println("Refresh using params:");
                System.out.println("\tQBN -> "+qbn);
                System.out.println("\tAP Acct -> "+ap_acct);
                System.out.println("\tAR Acct -> "+ar_acct);
                System.out.println("\tDef Bank Acct -> "+bank_acct);
                System.out.println("\tFY Start -> "+fy_start);

           		String accesstoken = (String)session.getAttribute("accessToken");
        		String accessstokensecret = (String)session.getAttribute("accessTokenSecret");
        		String realmID = (String)session.getAttribute("realmId");
        		String dataSource = (String)session.getAttribute("dataSource");
        		PlatformSessionContext context = webutils.getPlatformContext(accesstoken,accessstokensecret,realmID,dataSource);
		
	        	QueryManager qm = new QueryManager(context);
	      	    comp = GatherGL.fromDB(qm, qbn, ap_acct, ar_acct, bank_acct, fyCal);
   	     	    GatherGL.pushToDisk(comp, gl_file);
    	    	System.out.println("Wrote GL Serialized File: "+gl_file);

	    	    out.println("<p>General Ledger DB queried successfully:</p>");
		        out.println("<p>Company Name: <b>"+comp.getQbnName()+"</b></p>");

    		    out.print("<table>");
	      	    out.println("<col width='50'><col width='350'><col width='150'>");
	    	    out.print("<tr><th>Acct#</th><th>Name</th><th>Num GL Trans</th></tr>");
	    	    for (GLAccount acct : comp.getAccounts()) {
	    		    out.print("<tr>");
    	    		out.print("<td align='left'>" + acct.getAcctNum() + "</td>");
	        		out.print("<td align='center'>" + acct.getName() + "</td>");
	        		out.print("<td align='right'>" + acct.getTransactions().size() + "</td>");
	    	    	out.println("</tr>");	
	        	}
		        out.println("</table>");
            }
        } else {
            System.out.println("null params, holding off...");
        }
	} catch (Exception e) {		
		out.println("<p>Error: Servlet Exception thrown.</p>");
		System.out.println("Servlet Exception thrown: " + e.getMessage());
	}
%>

</body>
</html>
