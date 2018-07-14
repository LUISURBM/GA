package siga.ga.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class Logout extends HttpServlet {
    private static final long serialVersionUID = -4433102460849019660L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LogoutServlet :: doGet");
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+ "/");
    }
}
