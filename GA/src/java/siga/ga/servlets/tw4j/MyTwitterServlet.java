package siga.ga.servlets.tw4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import siga.ga.dao.TwitterStatus;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

/**
 * Servlet implementation class MyTwitterServlet
 * 
 * Notice that callback support requires Twitter4J 2.0.9
 * 
 */
@SuppressWarnings("serial")
public class MyTwitterServlet extends HttpServlet {
	private static final Log logger = LogFactory.getLog(MyTwitterServlet.class);
	private static String fileToken = File.separator + "WEB-INF"
			+ File.separator + "token.txt";

	private static final String INIT_PARAM_CONSUMER_KEY = "consumerKey";
	private static final String INIT_PARAM_CONSUMER_SECRET = "consumerSecret";
	private static final String INIT_PARAM_CALLBACK_URL = "callbackUrl";

	private static String consumerKey;
	private static String consumerSecret;
	private static String callbackUrl;

	private static final String PARAM_ACTION = "action";
	private static final String ACTION_SIGN_IN = "sign_in";
	private static final String ACTION_UPDATE = "update";
	private static final String ACTION_DELETE = "delete";
	private static final String ACTION_MORE = "more";
	private static final String ACTION_LATEST = "latest";
	private static final String PARAM_OAUTH_TOKEN = "oauth_token";
	private static final String PARAM_OAUTH_VERIFIER = "oauth_verifier";
	private static final String PARAM_STATUS = "status";

	private static final String ATTR_TWITTER = "twitter";
	private static final String ATTR_REQUEST_TOKEN = "request_token";
	private static final String ATTR_LAST_UPDATED_STATUS = "last_udpated_status";
	private static final String ATTR_LAST_UPDATED_STATUS_ID = "last_udpated_status_id";
	private static final String ATTR_FRIENDS_TIMELINES = "friends_timelines";
	private static final String ATTR_FRIENDS_TIMELINES_PAGE = "friends_timelines_page";

	private static final String PAGE_UPDATE_STATUS = "update_twitter_status.jsp";

	private static final String COOKIE_TWITTER_ID = "twitter_id";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyTwitterServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		consumerKey = config.getInitParameter(INIT_PARAM_CONSUMER_KEY).trim();
		consumerSecret = config.getInitParameter(INIT_PARAM_CONSUMER_SECRET)
				.trim();
		logger.debug("Consumer key retrieved from web.xml");
		logger.debug("Consumer secret retrieved from web.xml");

		callbackUrl = config.getInitParameter(INIT_PARAM_CALLBACK_URL);
		if (callbackUrl != null && !callbackUrl.trim().equals("")) {
			callbackUrl = callbackUrl.trim();
			logger.debug("Callback support is enabled, callback url: "
					+ callbackUrl);
		}

		fileToken = getServletContext().getRealPath("/") + fileToken;
		logger.debug("File to store / load access tokens: " + fileToken);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		// Call back from Twitter
		String oauthToken = request.getParameter(PARAM_OAUTH_TOKEN);
		if (oauthToken != null) {
			logger.debug(PARAM_OAUTH_TOKEN + " received from Twitter");
			try {
				Twitter twitter = (Twitter) session.getAttribute(ATTR_TWITTER);
				RequestToken requestToken = (RequestToken) session
						.getAttribute(ATTR_REQUEST_TOKEN);
				AccessToken accessToken;
				if (callbackUrl == null) {
					accessToken = twitter.getOAuthAccessToken(requestToken);
				} else {
					String oauthVerifier = request
							.getParameter(PARAM_OAUTH_VERIFIER);
					logger.debug(PARAM_OAUTH_VERIFIER
							+ " received from Twitter");
					accessToken = twitter.getOAuthAccessToken(requestToken
							.getToken(), requestToken.getTokenSecret(),
							oauthVerifier);
				}
				twitter.setOAuthAccessToken(accessToken);
				session.removeAttribute(ATTR_REQUEST_TOKEN);
				session.setAttribute(ATTR_TWITTER, twitter);

				int id = twitter.verifyCredentials().getId();
				logger.debug("Access token retrieved for user " + id
						+ " from Twitter");
				storeAccessToken(id, accessToken);
				Cookie cookie = new Cookie(COOKIE_TWITTER_ID, "" + id);
				cookie.setMaxAge(63072000); // Valid for 2 years
				response.addCookie(cookie);
				logger.debug("Cookie set for user " + id);

				// Get last status and friends' timelines
				getMyLastStatusAndStoreInSession(session);
				getFriendsTimelinesAndStoreInSession(session);

				// Go to the update status page
				request.getRequestDispatcher(PAGE_UPDATE_STATUS).forward(
						request, response);
			} catch (TwitterException e) {
				logger.error("Failed to retrieve access token - "
						+ e.getMessage());
				throw new ServletException(e);
			}
		}

		// Actions within this application
		String action = request.getParameter(PARAM_ACTION);
		if (ACTION_SIGN_IN.equals(action)) {
			logger.debug("Signing in with Twitter... "+consumerKey+" "+consumerSecret);
                        System.out.println("Signing in with Twitter... "+consumerKey+" "+consumerSecret);
			Twitter twitter = new Twitter();
			twitter.setOAuthConsumer(consumerKey, consumerSecret);

			// Try to load Twitter ID from cookies
			String id = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				Cookie cookie;
				for (int i = 0; i < cookies.length; i++) {
					cookie = cookies[i];
					if (COOKIE_TWITTER_ID.equals(cookie.getName())) {
						id = cookie.getValue();
					}
				}
			}

			// Try to load access token if user's Twitter ID is retrieved
			AccessToken accessToken = null;
			if (id != null) {
				accessToken = loadAccessToken(id);
				if (accessToken != null) {
					twitter.setOAuthAccessToken(accessToken);
					session.setAttribute(ATTR_TWITTER, twitter);

					// Get last status and friends' timelines
					try {
						getMyLastStatusAndStoreInSession(session);
						getFriendsTimelinesAndStoreInSession(session, true);
					} catch (TwitterException e) {
						e.printStackTrace();
					}

					// Access token loaded, go the up update status page
					logger.debug("Going to the status update page...");
					request.getRequestDispatcher(PAGE_UPDATE_STATUS).forward(
							request, response);
				}
			}

			// Can not load access token, go to Twitter for authentication
			if (accessToken == null) {
				try {
					RequestToken requestToken;
//					if (callbackUrl == null) {
						requestToken = twitter.getOAuthRequestToken();
//					} else {
//                                            System.out.println("MyTwitterServlet doPost callbackUrl :: "+callbackUrl+" "+twitter.getBaseURL());
//						requestToken = twitter
//								.getOAuthRequestToken(callbackUrl);
//					}
					String authorisationUrl = requestToken
							.getAuthorizationURL();
					session.setAttribute(ATTR_TWITTER, twitter);
					session.setAttribute(ATTR_REQUEST_TOKEN, requestToken);

					logger.debug("Redirecting user to " + authorisationUrl);
					response.sendRedirect(authorisationUrl);
				} catch (TwitterException e) {
					logger.error("Sign in with Twitter failed - "
							+ e.getMessage());
					throw new ServletException(e);
				}
			}

		} else if (ACTION_UPDATE.equals(action)) {
			logger.debug("Updating Twitter status...");
			String status = request.getParameter(PARAM_STATUS);
			Twitter twitter = (Twitter) session.getAttribute(ATTR_TWITTER);
			try {
				Status twitterStatus = twitter.updateStatus(status);
				logger.debug("Successfully updated the status to ["
						+ twitterStatus.getText() + "].");
				// Update last status
				session.setAttribute(ATTR_LAST_UPDATED_STATUS, twitterStatus
						.getText()
						+ " " + twitterStatus.getCreatedAt());
				session.setAttribute(ATTR_LAST_UPDATED_STATUS_ID, ""
						+ twitterStatus.getId());

				// Update friends' timelines
				getFriendsTimelinesAndStoreInSession(session, true);

				// Stay in the update status page
				logger.debug("Staying in the status update page...");
				request.getRequestDispatcher(PAGE_UPDATE_STATUS).forward(
						request, response);
			} catch (TwitterException e) {
				logger.error("Failed to update Twitter status - "
						+ e.getMessage());
				throw new ServletException(e);
			}
		} else if (ACTION_DELETE.equals(action)) {
			logger.debug("Deleteing Twitter status...");
			String strId = (String) session
					.getAttribute(ATTR_LAST_UPDATED_STATUS_ID);
			if (strId != null && strId != "") {
				Twitter twitter = (Twitter) session.getAttribute(ATTR_TWITTER);
				try {
					int id = twitter.verifyCredentials().getId();
					twitter.destroyStatus(Long.parseLong(strId));
					session.removeAttribute(ATTR_LAST_UPDATED_STATUS);
					session.removeAttribute(ATTR_LAST_UPDATED_STATUS_ID);
					logger.debug("Last update deleted for Twitter user " + id
							+ " deleted, session record removed");

					// Get last status and friends' timelines
					getMyLastStatusAndStoreInSession(session);
					getFriendsTimelinesAndStoreInSession(session, true);
				} catch (TwitterException e) {
					logger.error("Failed to delete Twitter status - "
							+ e.getMessage());
					throw new ServletException(e);
				}
			}

			// Stay in the update status page
			logger.debug("Staying in the status update page...");
			request.getRequestDispatcher(PAGE_UPDATE_STATUS).forward(request,
					response);
		} else if (ACTION_MORE.equals(action)) {
			// Update friends' timelines
			getFriendsTimelinesAndStoreInSession(session);

			// Stay in the update status page
			logger.debug("Staying in the status update page...");
			request.getRequestDispatcher(PAGE_UPDATE_STATUS).forward(request,
					response);
		} else if (ACTION_LATEST.equals(action)) {
			// Update friends' latest timelines
			getFriendsTimelinesAndStoreInSession(session, true);

			// Stay in the update status page
			logger.debug("Staying in the status update page...");
			request.getRequestDispatcher(PAGE_UPDATE_STATUS).forward(request,
					response);
		}
	}

	private synchronized AccessToken loadAccessToken(String id) {
		logger.debug("Trying to load access token for user " + id);
		AccessToken accessToken = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileToken));
			String line;
			String[] fields;
			while ((line = in.readLine()) != null) {
				fields = line.split(" ");
				if (id.equals(fields[0])) {
					accessToken = new AccessToken(fields[1], fields[2]);
					break;
				}
			}
		} catch (IOException e) {
			logger.error("Failed to load access token for " + id);
			e.printStackTrace();
			accessToken = null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					in = null;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			if (accessToken == null) {
				logger.debug("Access token for user " + id + " is null");
			} else {
				logger.debug("Access token loaded successfully for user " + id);
			}
		}

		return accessToken;
	}

	private synchronized void storeAccessToken(int id, AccessToken accessToken) {
		logger.debug("Trying to store access token for user " + id);
		// Store user ID, token and token secret
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(fileToken, true));
			out.append("" + id);
			out.append(" ");
			out.append(accessToken.getToken());
			out.append(" ");
			out.append(accessToken.getTokenSecret());
			out.newLine();
			logger.debug("Access token saved successfully for user " + id);
		} catch (IOException e) {
			logger.error("Failed to save access token for user " + id);
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					out = null;
				}
			}
		}
	}

	private void getMyLastStatusAndStoreInSession(HttpSession session)
			throws TwitterException {
		Twitter twitter = (Twitter) session.getAttribute(ATTR_TWITTER);
		// Get last updated status and save it in session
		List<Status> updatesList = twitter.getUserTimeline();
		if (!updatesList.isEmpty()) {
			Status status = updatesList.get(0);
			session.setAttribute(ATTR_LAST_UPDATED_STATUS, status.getText()
					+ " [" + status.getCreatedAt() + "]");
			session.setAttribute(ATTR_LAST_UPDATED_STATUS_ID, ""
					+ status.getId());
			logger.debug("Last update stored in session for Twitter user "
					+ twitter.verifyCredentials().getId());
		} else {
			logger.debug("Twitter user " + twitter.verifyCredentials().getId()
					+ " has no update available");
		}
	}

	private void getFriendsTimelinesAndStoreInSession(HttpSession session,
			boolean isLatestTimelinesRequired) {
		if (isLatestTimelinesRequired) { // Start from page 1
			session.removeAttribute(ATTR_FRIENDS_TIMELINES_PAGE);
			getFriendsTimelinesAndStoreInSession(session);
		}
	}

	private void getFriendsTimelinesAndStoreInSession(HttpSession session) {
		Twitter twitter = (Twitter) session.getAttribute(ATTR_TWITTER);
		Integer p = (Integer) session.getAttribute(ATTR_FRIENDS_TIMELINES_PAGE);
		int page = 1;
		if (p != null) {
			page = p + 1;
		}
		try {
			List<Status> l = twitter.getFriendsTimeline(new Paging(page, 20));
			TwitterStatus[] tss = new TwitterStatus[l.size()];
			for (int i = 0; i < tss.length; i++) {
				Status s = l.get(i);
				TwitterStatus ts = new TwitterStatus();
				ts.setCreatedDate(s.getCreatedAt());
				ts.setCreatedUser(s.getUser().getScreenName());
				ts.setId(s.getId());
				ts.setMessage(searchAndReplaceLink(searchAndReplaceLink(s
						.getText(), "http"), "https"));
				tss[i] = ts;
			}
			logger.debug(tss.length
					+ " friends' timeline retrieved, page number is " + page);

			session.setAttribute(ATTR_FRIENDS_TIMELINES, tss);
			session.setAttribute(ATTR_FRIENDS_TIMELINES_PAGE, page);
		} catch (TwitterException e) {
			logger.error("Failed to get friends' timeline for "
					+ twitter.getUserId() + " - " + e.getMessage());
		}
	}

	private static String searchAndReplaceLink(String message, String protocol) {
		// Message returned by Twitter does not have proper HTTP(S) links
		// So construct them here
		if (message == null || protocol == null) {
			return message;
		}

		protocol = protocol.trim();
		if (protocol.indexOf("://") == -1) {
			protocol = " " + protocol + "://";
		}

		int idx1, idx2;
		StringBuilder sb = new StringBuilder();
		while ((idx1 = message.indexOf(protocol)) != -1) {
			sb.append(message.substring(0, idx1 + 1));
			message = message.substring(idx1 + 1);
			if ((idx2 = message.indexOf(' ')) != -1) {
				String str = message.substring(1, idx2);
				sb.append("<a href=\"").append(str).append(
						"\" target=\"_blank\">").append(str).append("</a>");
				message = message.substring(idx2);
			} else {
				sb.append("<a href=\"").append(message).append(
						"\" target=\"_blank\">").append(message).append("</a>");
				message = "";
			}
		}
		sb.append(message);

		return sb.toString();
	}

}
