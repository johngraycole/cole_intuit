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
package com.intuit.bluedot;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.intuit.platform.client.PlatformClient;
import com.intuit.platform.client.PlatformSessionContext;
import com.intuit.platform.util.Logger;
import com.intuit.utils.WebUtils;

/*
 * This class implements the disconnect functionality, which invalidates the OAuth access token.
 * The app is no longer authorized to access the user's QuickBooks data.
 * Subsequent requests with this invalid token will return an error.  
 */
public class DisconnectServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("#### DisconnectServlet ####");
		HttpSession session = request.getSession();

		WebUtils webutils = new WebUtils();
		String accesstoken = (String) session.getAttribute("accessToken");
		String accessstokensecret = (String) session
				.getAttribute("accessTokenSecret");
		String realmID = (String) session.getAttribute("realmId");
		String dataSource = (String) session.getAttribute("dataSource");
		PlatformSessionContext context = null;
		//PrintWriter out = response.getWriter();

		try {
			if (accesstoken != null && accessstokensecret != null
					&& realmID != null) {
				context = webutils.getPlatformContext(accesstoken,
						accessstokensecret, realmID, dataSource);
				PlatformClient pClient = new PlatformClient();

				pClient.disconnect(context);

			}
		} catch (Exception e) {
			Logger.getInstance().error(e.getMessage());
		}

		// In a production app, the access token values would be stored in a
		// persistent store, where their values would be set to null. In this sample app,
		// the values are saved only as session attributes.

		session.setAttribute("accessToken", null);
		session.setAttribute("accessTokenSecret", null);
		session.setAttribute("connectionStatus", "not_authorized");
		System.out.println("#### DisconnectServlet leaving now.... ####");

		response.sendRedirect("/Cole_Intuit/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
