package siga.ga.servlets.fb;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Login :: doGet");
    	String callbackURL = null;
    	String additionalPermissions = getServletContext().getInitParameter("additionalPermissions");
    	
        try {
            StringBuffer callbackURLbuffer = request.getRequestURL();
            int index = callbackURLbuffer.lastIndexOf("/");
            callbackURLbuffer.replace(index, callbackURLbuffer.length(), "").append("/callback");
            System.out.println("Login doGet callbackURLbuffer :: "+callbackURLbuffer);
            callbackURL = URLEncoder.encode(callbackURLbuffer.toString(), "UTF-8");
            
            String facebookAppId = getServletContext().getInitParameter("facebookAppId");
            System.out.println("LoginServlet doGet callbackURL encode :: "+callbackURL);
            String authURL = "https://graph.facebook.com/oauth/authorize?client_id=" + facebookAppId + 
            		"&redirect_uri=" +
                    callbackURL + 
                    "&scope=" + additionalPermissions;
           
            System.out.println("LoginServlet doGet authURL: " + authURL);
            
            response.sendRedirect(authURL);

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }
}
