/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package siga.ga.controllers;

import org.springframework.social.facebook.api.FacebookProfile;

import org.springframework.social.facebook.api.PagedList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import siga.ga.entytis.GaDcm;
import siga.ga.jsfclasses.util.JsfUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import siga.ga.entytis.Archivo;
import siga.ga.entytis.Personaje;

/**
 *
 * @author Otros
 * 
 */
@SessionScoped
@Controller
@RequestMapping("/")
public class SigaMenuController implements Serializable {

    public SigaMenuController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    @PersistenceUnit(unitName = "GAPU")
    private EntityManagerFactory emf = null;
    @Resource
    private UserTransaction utx = null;
    private transient String panelActivo;
    private transient String accion = "";
    private Personaje usuarioActivo;
    private List<GaDcm> tendencias;
   private Facebook facebook;
   private static boolean popUpLogin = false;
   private int rows;
   
    @Inject
    public SigaMenuController() {
        this.rows = 0;
        System.out.println("MenuController init :: inicio ");
        
        SigaMenuController.popUpLogin = siga.ga.jsfclasses.util.JsfUtil.validarSesion();
        
        this.usuarioActivo = siga.ga.jsfclasses.util.JsfUtil.usuarioActivo();
        
        System.out.println("MenuController init :: validaSesion "+SigaMenuController.popUpLogin);
        
        this.panelActivo = "main";
        
        try {
            InitialContext ic = new InitialContext();
            this.utx = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            this.emf = javax.persistence.Persistence.createEntityManagerFactory("GAPU");
        } catch (NamingException ex) {
            Logger.getLogger(SigaMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.tendencias = new ArrayList<GaDcm>();
        this.tendencias.add(
                new GaDcm("GESTION ARCHIVISTICA"));
        this.tendencias.add(
                new GaDcm("GESTION AMBIENTAL"));
        this.tendencias.add(
                new GaDcm("JAVA ORACLE"));
        this.tendencias.add(
                new GaDcm("JAVA ORACLE PROGRAMER"));
        this.tendencias.add(
                new GaDcm("PLSQL"));
        this.tendencias.add(
                new GaDcm("IMB"));
        this.tendencias.add(
                new GaDcm("JSE"));
        this.tendencias.add(
                new GaDcm("J2EE"));
        this.tendencias.add(
                new GaDcm("GLASSFISH"));
        this.tendencias.add(
                new GaDcm("ICEFACES"));
        this.tendencias.add(
                new GaDcm("TOMCAT"));
        this.tendencias.add(
                new GaDcm("C#"));
        this.tendencias.add(
                new GaDcm("THE STROKES"));
        this.tendencias.add(
                new GaDcm("FACEBOOK"));
        this.tendencias.add(
                new GaDcm("PINTERESR"));
        this.tendencias.add(
                new GaDcm("APPS.CO"));
        this.tendencias.add(
                new GaDcm("CODECADEMY"));
        this.tendencias.add(
                new GaDcm("UNINCCA"));
        this.tendencias.add(
                new GaDcm("UNIANDES"));
        this.tendencias.add(
                new GaDcm("YALE"));
        this.tendencias.add(
                new GaDcm("UNAL COLOMBIA"));
        this.tendencias.add(
                new GaDcm("ARCHIVO NACIONAL"));
        this.tendencias.add(
                new GaDcm("IDEAM"));
        this.tendencias.add(
                new GaDcm("MINEDU"));
        this.tendencias.add(
                new GaDcm("MINTIC"));
        this.tendencias.add(
                new GaDcm("ORGANIC DESIGN"));
        this.tendencias.add(
                new GaDcm("EVERYONESMIXTAPE"));
        this.tendencias.add(
                new GaDcm("SKYDRIVE"));
        this.tendencias.add(
                new GaDcm("IEEE"));
        this.tendencias.add(
                new GaDcm("GG BBS TERMINAL"));
        this.tendencias.add(
                new GaDcm("ARCHIVISTIC METAPRESS"));
System.out.println("MenuController init :: fin");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String getPanelActivo() {
        return panelActivo;
    }

    private static String getURL() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return req.getQueryString();
    }

    public void setPanelActivo(String panelActivo) {
        System.out.println("MenuController :: setPanel " + panelActivo);
        this.panelActivo = panelActivo;
    }

    public void accion(ActionEvent e) {
        JsfUtil.validarSesion();
        this.panelActivo = "panelCuenta";
        System.out.println("" + e.toString());

        //sr.
    }

    public List<GaDcm> getTendencias() {
        return tendencias;
    }
    
    

    public void setTendencias(List<GaDcm> tendencias) {
        this.tendencias = tendencias;
    }

    public boolean isPanelCuentaActiva() {
        return this.panelActivo.contains("panel") ? true : false;
    }
public void closeEvent(ActionEvent event) {
        popUpLogin = false;
    }
public void toggleOpened(ActionEvent event) {
        popUpLogin = !popUpLogin;
    }
public static void validarSesion(ActionEvent event) {
        JsfUtil.validarSesion();
    }
    public String getUserFromSession() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String userName = (String) session.getAttribute("FACEBOOK_USER");
        if (userName != null) {
            return "Hello " + userName;
        } else {
            return "";
        }
    }

    public String getFacebookUrlAuth() {
        HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String sessionId = session.getId();
        String appId = "1432236540324653";
        String redirectUrl = "http://localhost:7078/GA/jsfpages/inicio.jsf";
        String returnValue = "https://www.facebook.com/dialog/oauth?client_id="
                + appId + "&redirect_uri=" + redirectUrl
                + "&scope=email,user_birthday&state=" + sessionId;
        return returnValue;
    }
    

    

    @RequestMapping(method = RequestMethod.GET)
    public String helloFacebook(Model model) {
        if (!this.facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }

        model.addAttribute(this.facebook.userOperations().getUserProfile());
        PagedList<FacebookProfile> friends = this.facebook.friendOperations().getFriendProfiles();
        model.addAttribute("friends", friends);

        return "hello";
    }

    public boolean isPopUpLogin() {
        return popUpLogin;
    }

    public void setPopUpLogin(boolean popUpLogin) {
        this.popUpLogin = popUpLogin;
    }

    public Personaje getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(Personaje usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    
    
}

