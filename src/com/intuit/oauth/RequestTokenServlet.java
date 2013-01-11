/*
* Copyright (c) 2011 Intuit, Inc.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.opensource.org/licenses/eclipse-1.0.php
* Contributors:
*
*    Intuit Partner Platform - initial contribution 
*
*/

package com.intuit.oauth;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.intuit.oauth.OauthHelper;

/*
 * This servlet begins the three-legged OAuth flow.  It retrieves the OAuth request token and secret.
 * In the OAuth 1.0A Spec, this servlet corresponds to the step, "Obtaining an Unauthorized Request Token." 
 */

public class RequestTokenServlet extends HttpServlet{

	public RequestTokenServlet()
	{
		super();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("#### RequestTokenServlet ####");
		// Invoke the helper class and retrieve the request token. 
		OauthHelper oauthhelper = new OauthHelper();
		Map<String,String> requesttokenmap = oauthhelper.getRequestToken();
		
		HttpSession session = request.getSession();
		session.setAttribute("requestToken",requesttokenmap.get("requestToken"));
		session.setAttribute("requestTokenSecret",requesttokenmap.get("requestTokenSecret"));
		
		//Retrieve the Authorize URL 
		String authURL = oauthhelper.getAuthorizeURL(requesttokenmap.get("requestToken"),requesttokenmap.get("requestTokenSecret"));
		
		System.out.println("#### RequestTokenServlet leaving now....");
		//Redirect to the authorized URL page and retrieve the verifier code.
		response.sendRedirect(authURL);
				
	}

}
