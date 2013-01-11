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
 * This servlet gets the OAuth access token from Intuit.  A valid access token indicates that the user
 * has authorized the app to access the user's QuickBooks data.  Your app should store the access token
 * in a persistent store so that it can be used for later requests.
 * In the OAuth 1.0A Spec, this servlet corresponds to the step, "Obtaining an Access Token."
 */

public class AccessTokenServlet extends HttpServlet {

	public AccessTokenServlet() {
		super();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("#### AccessTokenServlet ####");
		HttpSession session = request.getSession();
		String verifierCode = request.getParameter("oauth_verifier");
		String realmID = request.getParameter("realmId");
		session.setAttribute("realmId", realmID);
		String dataSource = request.getParameter("dataSource");
		session.setAttribute("dataSource", dataSource);

		String requestToken = (String) session.getAttribute("requestToken");
		String requestTokenSecret = (String) session
				.getAttribute("requestTokenSecret");

		System.out.println("verifier code:  " + verifierCode);
		System.out.println("realmID:  " + realmID);
		System.out.println("dataSource:  " + dataSource);

		OauthHelper oauthhelper = new OauthHelper();
		System.out.println("before calling Access token API");
		Map<String, String> accesstokenmap = oauthhelper.getAccessToken(
				verifierCode, requestToken, requestTokenSecret);
		System.out.println("after calling Access token API");

		session.setAttribute("accessToken", accesstokenmap.get("accessToken"));
		session.setAttribute("accessTokenSecret", accesstokenmap
				.get("accessTokenSecret"));
		session.setAttribute("connectionStatus", "authorized");
		
		String flowType = (String)session.getAttribute("flowType");
		String redirectPage;
		
		// If the user authorized with the Connect to Intuit button, then redirect to inter.jsp.
		// Otherwise, redirect to dashboard.jsp.
		
		System.out.println ("flowType = "  + flowType);
		
		if (flowType == null) {
			redirectPage = "dashboard.jsp";
		}
		else if (flowType.equalsIgnoreCase("connect_button")) {
			redirectPage = "inter.jsp";
		}
		else {
			redirectPage = "dashboard.jsp";
		}
					
		// The OAuth flow has been completed, so go to the app.
		System.out.println("#### AccessTokenServlet leaving now....going to " + redirectPage);
		response.sendRedirect("/Cole_Intuit/" + redirectPage);
	}
}
