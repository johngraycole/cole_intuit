package com.intuit.openid;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;

import com.intuit.utils.WebUtils;

/**
 * This servlet initiates the OpenID login by sending an authentication request to the Intuit OpenID service.
 * The URL of this servlet is specified on the app's Publish tab in the UI of My Developer Center.
 */
public class LoginInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// The URL for the Intuit OpenID Provider service:
	public static String OPENID_PROVIDER_URL;
		
	public LoginInitServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println(" #### LoginInitServlet  #### ");
		WebUtils prop = new WebUtils();
		OPENID_PROVIDER_URL = prop.getOpenIdProviderUrl();
		System.out.println("OpenID Provider URL: " + OPENID_PROVIDER_URL);
		ConsumerManager manager = new ConsumerManager();
		
		manager.setAssociations(new InMemoryConsumerAssociationStore());
        manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
        manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);

		DiscoveryInformation discovered = null;

		try {
			System.out.println("OpenID Provider URL = " + OPENID_PROVIDER_URL);
			discovered = new DiscoveryInformation(
					new URL(OPENID_PROVIDER_URL));
		} catch (DiscoveryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<DiscoveryInformation> discoveries = new ArrayList<DiscoveryInformation>();
		discoveries.add(discovered);

		final DiscoveryInformation discoveryInfo = manager.associate(discoveries);
		request.getSession().setAttribute("openid-disc", discoveryInfo);

		FetchRequest fetch = FetchRequest.createFetchRequest();

		try {
			fetch.addAttribute("FirstName", "http://axschema.org/namePerson/first",
					true);
			fetch.addAttribute("LastName", "http://axschema.org/namePerson/last",
					true);
			fetch.addAttribute("Email", "http://axschema.org/contact/email", true);
			// the following for getting the current realmId for the user. You can use other namespaces too, like:
			// http://schema.openid.net/intuit/realmId
			// http://openid.net/schema/intuit/realmId
			fetch.addAttribute("RealmId", "http://axschema.org/intuit/realmId", true);
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get up to three email addresses.
		fetch.setCount("Email", 3);

		// Obtain an AuthRequest message to be sent to the OpenID provider.
		AuthRequest authReq = null;
		String openIdReturnUrl = prop.getOpenIdReturnUrl();
		System.out.println("openIdReturnUrl = " + openIdReturnUrl);
		try {
			authReq = manager.authenticate(discoveryInfo, openIdReturnUrl);
			authReq.addExtension(fetch);
		} catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpSession session = request.getSession();
		session.setAttribute("consumerManager", manager);
		System.out.println("authReq.getDestinationUrl: " + authReq.getDestinationUrl(true));
		System.out.println(" #### LoginInitServlet leaving now... ");

		response.sendRedirect(authReq.getDestinationUrl(true));
	} 

}


