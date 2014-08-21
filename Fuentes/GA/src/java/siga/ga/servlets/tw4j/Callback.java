package siga.ga.servlets.tw4j;


import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
//import twitter4j.auth.RequestToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import twitter4j.User;
//import twitter4j.auth.AccessToken;

public class Callback extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        try {
            //        System.out.println("CallbackServlet :: doGet");
            //        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
            //        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
            //        String verifier = request.getParameter("oauth_verifier");
            //        try {
            //            twitter.getOAuthAccessToken(requestToken, verifier);
            //            request.getSession().removeAttribute("requestToken");
            //        } catch (TwitterException e) {
            //            throw new ServletException(e);
            //        }
            //        response.sendRedirect(request.getContextPath() + "/");
//                    Twitter twitter = (Twitter) request.getSession().getAttribute(
//             "twitter");
//                     RequestToken requestToken = (RequestToken) request.getSession()
//             .getAttribute("requestToken");
//             String verifier = request.getParameter("oauth_verifier");
//             AccessToken accessToken = null;
//             try {
//             accessToken = twitter.getOAuthAccessToken(requestToken,
//             verifier);
//             request.getSession().removeAttribute("requestToken");
//             
//             } catch (TwitterException twitterException) {
//             twitterException.printStackTrace();
//             }
//                    long userId = accessToken.getUserId();
//             User user = twitter.showUser(userId);
//            String avatarUrl = user.getProfileImageURL().toString();
//             System.out.println("Callback doGet :: "+userId+" "+user.getScreenName()+" "+avatarUrl);
//        } catch (TwitterException ex) {
//            Logger.getLogger(Callback.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
