package com.intuit.openid;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openid4java.discovery.Identifier;

/**
 * This servlet handles the response from the authentication request that was sent to the Intuit
 * OpenID provider service by the LoginInitServlet.
 * The primary purpose of this servlet is to verify the request.  It also extracts these parameters
 * from the request: identity, first name, last name, and email.  A production app could store these 
 * values in a persistent store for later use.
 */
public class VerifyOpenIDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyOpenIDServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println(" #### VerifyOpenIDServlet ####");

	    HttpSession session = request.getSession();
	    OpenIDHelper openIDHelper = new OpenIDHelper();
	    
		Identifier identifier = openIDHelper.verifyResponse(request);
		System.out.println("OpenID identifier:"	+ ((identifier == null) ? "null" : identifier.getIdentifier()));
		
		String identity = request.getParameter("openid.identity");
		System.out.println("openid.identity: " + identity);
		
		String firstName = request.getParameter("openid.alias3.value.alias1");
		System.out.println("openid.alias3.value.alias1: " + firstName);

		String lastName = request.getParameter("openid.alias3.value.alias2");
		System.out.println("openid.alias3.value.alias2: " + lastName);

		String email = request.getParameter("openid.alias3.value.alias3");
		System.out.println("openid.alias3.value.alias3: " + email);
		
		String realmId = request.getParameter("openid.alias3.value.alias4");
		System.out.println("openid.alias3.value.alias4: " + realmId);
						
		session.setAttribute("openIDidentity", identity);
		session.setAttribute("firstName", firstName);
		session.setAttribute("lastName", lastName);
		session.setAttribute("email", email);
		session.setAttribute("openidstatus", "verified");
	
			String connectionStatus = (String)session.getAttribute("connectionStatus");
		if (connectionStatus == null) {
			session.setAttribute("connectionStatus", "not_authorized");
			connectionStatus = "not_authorized";
		}
		 
		System.out.println(" #### VerifyOpenIDServlet leaving now...");
		
		if(connectionStatus.equalsIgnoreCase("authorized")) {
			// The user has already connected this app to QuickBooks, so go to
			// the dashboard.
			response.sendRedirect("/Cole_Intuit/dashboard.jsp");
		 }
		else {
			// Redirect to start the OAuth flow.
			session.setAttribute("connectionStatus", "not_authorized");
			response.sendRedirect("/Cole_Intuit/directconnect.jsp");
		}
	} // doPost
}
