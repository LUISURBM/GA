package siga.ga.servlets.tw4j;



import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
//import twitter4j.TwitterFactory;
//import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
//import twitter4j.conf.Configuration;
//import twitter4j.conf.ConfigurationBuilder;

@SuppressWarnings("serial")
public class Login extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException 
    {
//        try {
            //        System.out.println("Login :: doGet");
            //         Twitter twitter = new TwitterFactory().getInstance();
            //        request.getSession().setAttribute("twitter", twitter);
            //        try {
            //            StringBuffer callbackURLbuffer = request.getRequestURL();
            //            String callbackURL = null;
            //            int index = callbackURLbuffer.lastIndexOf("/");
            //            callbackURLbuffer.replace(index, callbackURLbuffer.length(), "").append("/callbackTW");
            //            System.out.println("Login doGet callbackURL ::  "+callbackURLbuffer.toString());
            //            String facebookAppId = getServletContext().getInitParameter("facebookAppId");
            //            callbackURL = URLEncoder.encode(callbackURLbuffer.toString(), "UTF-8");
            //            System.out.println("Login doGet callbackURL encode ::  "+callbackURL);
            //            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL);
            //            request.getSession().setAttribute("requestToken", requestToken);
            //            System.out.println("Login doGet requestToken :: "+requestToken.getAuthenticationURL());
            //            response.sendRedirect(
            //                    requestToken.getAuthenticationURL()
            ////                    "http://twitter.com/oauth/request_token&oauth_consumer_key=GZo3765X3787gLrr3QZQlg&oauth_nonce=5b635fdf11cae0734cd80789ad5679e0&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1287765191"
            //                    );
            //
            //        } catch (TwitterException e) {
            //            System.out.println("Login doGet :: "+e.getMessage());
            //        }
//                    ConfigurationBuilder builder = new ConfigurationBuilder();
//builder.setOAuthConsumerKey("GZo3765X3787gLrr3QZQlg");
//builder.setOAuthConsumerSecret("Pt6VBmVd6pUQvGNKQAwyMFX0rDzSKQgOVDQ4Bth2Hk");
//Configuration configuration = builder.build();
//TwitterFactory factory = new TwitterFactory(configuration);
//Twitter twitter = factory.getInstance();

////Twitter twitter = TwitterFactory().getInstance();
//twitter.setOAuthConsumer("GZo3765X3787gLrr3QZQlg", "Pt6VBmVd6pUQvGNKQAwyMFX0rDzSKQgOVDQ4Bth2Hk");
//RequestToken twitterRequestToken
//                 = twitter.getOAuthRequestToken();
             
//             RequestToken requestToken;
//             request.getSession().setAttribute("twitter", twitter);

//             requestToken = twitter.getOAuthRequestToken(
//                     URLEncoder.encode("http://cuervo.nixiweb.com/GA/callbackTW", "UTF-8")
////                     "http://cuervo.nixiweb.com/GA/welcome.jsf"
//                     );
//                            requestToken.getAuthorizationURL();
//             String authURL = twitterRequestToken.getAuthenticationURL();
//             request.getSession().setAttribute("requestToken", twitterRequestToken);
//             response.sendRedirect(authURL);
//       
// } catch (TwitterException ex) {
//            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
