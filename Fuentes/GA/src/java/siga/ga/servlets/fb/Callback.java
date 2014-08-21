package siga.ga.servlets.fb;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siga.ga.dao.FBUser;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import siga.ga.controllers.PersonajeJpaController;
import siga.ga.entytis.Personaje;

public class Callback extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static String getAccessTokenFromWebContent(String webContent) {
        String accessToken = null;
        int s = webContent.indexOf("access_token=") + ("access_token=".length());
        int e = webContent.indexOf("&");
        accessToken = webContent.substring(s, e);
        return accessToken;
    }
    
    private static String getWebContentFromURL(String webnames) {
        try {
            URL url = new URL(webnames);
            URLConnection urlc = url.openConnection();
            //BufferedInputStream buffer = new BufferedInputStream(urlc.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF8"));
            StringBuffer builder = new StringBuffer();
            int byteRead;
            while ((byteRead = buffer.read()) != -1) {
                builder.append((char) byteRead);
            }
            buffer.close();
            String text = builder.toString();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CallbackServlet :: doGet");
        String code = null;
        String facebookAppId = getServletContext().getInitParameter("facebookAppId");
        String facebookAppSecret = getServletContext().getInitParameter("facebookAppSecret");
        String redirectURL = null;
        String accessURL = null;
        String accessToken = null;
        String webContent = null;
        
        try {
            StringBuffer redirectURLbuffer = request.getRequestURL();
            int index = redirectURLbuffer.lastIndexOf("/");
            redirectURLbuffer.replace(index, redirectURLbuffer.length(), "").append("/callback");
            System.out.println("CallbackServlet doGet callbackURLbuffer :: " + redirectURLbuffer);
            redirectURL = URLEncoder.encode(redirectURLbuffer.toString(), "UTF-8");
            
            code = request.getParameter("code");
            System.out.println("CallbackServlet :: code " + code);
            System.out.println("CallbackServlet :: redirectURL " + redirectURL);
            if (null != code) {
//        		System.out.println("Code: " + code);
                
                accessURL = "https://graph.facebook.com/oauth/access_token?client_id=" + facebookAppId
                        + "&redirect_uri="
                        + //http://corax.no-ip.info
                        redirectURL
                        + "&client_secret=" + facebookAppSecret + "&code=" + code;
//        		System.out.println("accessURL: " + accessURL);
                System.out.println("CallbackServlet doGet :: accessURL " + accessURL);
                webContent = getWebContentFromURL(accessURL);
                System.out.println("CallbackServlet doGet :: webContent " + webContent);
//        		System.out.println("accessURL: " + webContent);
                accessToken = getAccessTokenFromWebContent(webContent);
                System.out.println("CallbackServlet doGet :: accessToken " + accessToken);
            } else {
                response.sendRedirect(request.getContextPath() + "/error.html");
                return;
            }
            
            System.out.println("CallbackServlet :: accesToken " + accessToken);
            if (null != accessToken) {
//            	System.out.println("accessToken: " + accessToken);
                
                FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
                
                User user = facebookClient.fetchObject("me", User.class);
                
                Personaje p = PersonajeJpaController.finByFbId(user.getId());
                
                if (null == p.getId()) {
                    p.setCedula("");
                    p.setTelefono("");
                }
                String hometownName = "";
                System.out.println(""+user.getId());System.out.println(""+user.getName());System.out.println(""+user.getLastName());System.out.println(""+user.getFirstName());
                        System.out.println(""+user.getGender());System.out.println(""+user.getEmail());System.out.println(""+user.getBirthday());
                        
                        try{
                        
                       hometownName = user.getHometown().getName();
                        }catch(NullPointerException npe){
                            hometownName = "";
                        }
                        
                        String locationName = "";
                        try{
                            locationName = user.getLocation().getName();
                        }catch(NullPointerException npe){
                        
                        locationName="";}
                        
                        String bio = "";
                        try{
                            locationName = user.getBio();
                        }catch(NullPointerException npe){
                        
                        bio="";}
                        
                        String about = "";
                        try{
                            about = user.getAbout();
                        }catch(NullPointerException npe){
                        
                        about="";}
                        
                        String link = "";
                        try{
                            link = user.getLink();
                        }catch(NullPointerException npe){
                        
                        link="";}
                        
                        String locale = "";
                        try{
                            locale = user.getLocale();
                        }catch(NullPointerException npe){
                        
                        locale="";}
                        
                        String relationshipStatus = "";
                        try{
                            relationshipStatus = user.getRelationshipStatus();
                        }catch(NullPointerException npe){
                        
                        relationshipStatus="";}
                        
                        String interestedIn = "";
                        try{
                            interestedIn = user.getInterestedIn().toString();
                        }catch(NullPointerException npe){
                        
                        interestedIn="";}
                        
                        String cedula = "";
                        try{
                            cedula = p.getCedula();
                        }catch(NullPointerException npe){
                        
                        cedula="";}
                        
                        String telefono = "";
                        try{
                            telefono = p.getTelefono();
                        }catch(NullPointerException npe){
                        
                        telefono="";}
                
                FBUser fbUser = new FBUser(user.getId(), user.getName(), user.getLastName(), user.getFirstName(),
                        user.getGender(), user.getEmail(), user.getBirthday(),  hometownName ,
                        locationName, bio, about, link, locale,
                        relationshipStatus, interestedIn, cedula, telefono);
                
                request.getSession().setAttribute("fbUser", fbUser);
//                System.out.println("Callback :: "+user.getEmail());
//            	System.out.println("User object: " + user.toString());
                response.sendRedirect(request.getContextPath() + "/welcome.jsf");
            }
            
            if (null == accessToken) {
                response.sendRedirect(request.getContextPath() + "/error.html");
            }
            
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error.html");
            throw new ServletException(e);
        }
        
    }
}
