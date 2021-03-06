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

package com.intuit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.intuit.platform.client.PlatformServiceType;
import com.intuit.platform.client.PlatformSessionContext;
import com.intuit.platform.client.security.OAuthCredentials;

/*
 * A utility class for this sample web app.
 */

public class WebUtils {

	public static Properties propconfig = null;
	public static String PROP_FILE = "/cole_intuit.properties";

	private void initProps() {
		// Get the values from the app's properties file.
		if (propconfig == null) {
			propconfig = new Properties();
			InputStream fileInputStream = null;
			fileInputStream = this.getClass().getResourceAsStream(PROP_FILE);
			if (fileInputStream != null) {
				try {
					propconfig.load(fileInputStream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						fileInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	private String getProps(String key) {
		String value = "";
		initProps();
		if (propconfig != null) {
			value = propconfig.getProperty(key);
		}
		return value;
	}

	public String getGLSerializedFile() {
		return getProps("gl_serialized_file");
	}

	public String getappToken() {
		return getProps("appToken");
	}

	public String getDBID() {
		return getProps("appDBID");
	}

	public String getOpenIdReturnUrl() {
		return getProps("openid_return_url");
	}
	
	public String getOpenIdProviderUrl() {
		return getProps("openid_provider_url");
	}
	
	public String getOauthCallbackUrl() {
		return getProps("oauth_callback_url");
	}

	public String getConsumerKey() {
		String oauth_consumer_key = getProps("oauth_consumer_key");
		return oauth_consumer_key;
	}

	public String getConsumerSecret() {
		String oauth_consumer_secret = getProps("oauth_consumer_secret");
		return oauth_consumer_secret;
	}

	public String getOauthUrl() {
		String oauth_url = getProps("oauth_url");
		return oauth_url;
	}
	
	public String getAppcenterUrl() {
		String appcenter_url = getProps("appcenter_url");
		return appcenter_url;
	}
	
	public String getAppUrl() {
		String app_url = getProps("app_url");
		return app_url;
	}


	public PlatformSessionContext getPlatformContext(String accesstoken,
			String accesstokensecret, String realmID, String dataSource) {

		// The PlatformSessionContext is required for calls to Data Services, which
		// access QuickBooks data.

		// The apptoken, consumer key, and consumer secret are from the app's properties file.
		// These values were generated when you created the app in Intuit App Center.
		// The realmID, which uniquely identifies the QuickBooks company, is also generated by Intuit.
		// The dataSource indicates whether the QuickBooks data is from QuickBooks Online (QBO)
		// or QuickBooks Desktop (QBD).
		// The realmID and dataSource are parameters in the request to AccessTokenServlet, which saves
		// them as session attributes.
		
		String apptoken = getappToken();
		
		System.out.println("apptoken inside getPlatformSessionContext: "
				+ apptoken);
		System.out.println("realmID inside getPlatformSessionContext: " + realmID);
		System.out
		.println("OAuth acccess token inside getPlatformSessionContext: "
				+ accesstoken);
				
		PlatformServiceType platformServiceType;
		if (dataSource.equalsIgnoreCase("QBO")) {
			platformServiceType = PlatformServiceType.QBO;
		}
		else {
			platformServiceType = PlatformServiceType.QBD;
		}

		OAuthCredentials oauthcredentials = new OAuthCredentials(
				getConsumerKey(), getConsumerSecret(), accesstoken,
				accesstokensecret);
		PlatformSessionContext context = new PlatformSessionContext(
				oauthcredentials, apptoken, platformServiceType, realmID);
		
		return context;
	}
}
