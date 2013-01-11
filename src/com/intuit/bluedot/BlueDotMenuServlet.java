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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.intuit.platform.client.PlatformClient;
import com.intuit.platform.client.PlatformSessionContext;
import com.intuit.utils.WebUtils;

/*
 * This is the proxy class for generating the Intuit "blue dot" menu, which shows the Intuit Anywhere
 * apps available to the user.  To generate the menu, the doGET method calls PlatformClient.getAppMenu()
 * (a wrapper for the the AppMenu Web API of Intuit Anywhere), which returns the HTML for the menu.  
 * In this sample app, the call to getAppMenu() is made whenever the user selects the menu, making the menu 
 * display a little slow.  In a production app, we could improve performance by caching the HTML returned 
 * by the call to getAppMenu(), because the contents of the menu does not change often.
 */

public class BlueDotMenuServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Retrieve the credentials from the session. In a production app these
		// would be retrieved from a persistent store.
		System.out.println("### BlueDotMenuServlet ###");
		HttpSession session = request.getSession();
		WebUtils webutils = new WebUtils();
		String accesstoken = (String) session.getAttribute("accessToken");
		String accessstokensecret = (String) session
				.getAttribute("accessTokenSecret");
		String realmID = (String) session.getAttribute("realmId");
		String dataSource = (String) session.getAttribute("dataSource");

		PlatformSessionContext context = null;
		PrintWriter out = response.getWriter();

		try {
			if (accesstoken != null && accessstokensecret != null
					&& realmID != null) {
				context = webutils.getPlatformContext(accesstoken,
						accessstokensecret, realmID, dataSource);
				PlatformClient pClient = new PlatformClient();
				StringBuffer sb = new StringBuffer();
				List<String> menuList = pClient.getAppMenu(context);
				if (menuList != null) {
					for (String mItem : menuList) {
						sb.append(mItem);
						// System.out.println("appMenu: " + mItem);
						out.println(mItem);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in BlueDotMenuServlet "
					+ e.getMessage());
		}

		System.out.println("#### BlueDotMenuServlet leaving now....####");
	} // doGet

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
