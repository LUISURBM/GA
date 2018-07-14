package siga.ga.jsfclasses.util;

import siga.ga.dao.FBUser;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.icepush.client.HttpRequest;
import siga.ga.controllers.PersonajeJpaController;
import siga.ga.entytis.Personaje;

public class JsfUtil {

    private static final String FBAppSecretId = "1622c529f8d33772d80847ee892db1d3";
    private static final String FBAppId = "1432236540324653";
    private static final String FBAccessToken = "1432236540324653|UAcKvw_ph0rXUpDY8CIoOPqiO88";

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static boolean validarSesion() {

            Personaje p =  usuarioActivo() ;
            
                if ("".equals(p.getCedula()) || "".equals(p.getTelefono())) {
                    return true;//Registrado en SIGAPU
                } else {
                    return false;//Registrado en SIGAPU
                }

    }

    public static FBUser obtenerFBUser() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return (FBUser) request.getSession().getAttribute("fbUser");
    }

    public static Personaje usuarioActivo() {
        FBUser fetchObject = obtenerFBUser();

            System.out.println("JsfUtil validarSesion :: " + fetchObject.getId());

            return PersonajeJpaController.finByFbId(fetchObject.getId());
    }
}