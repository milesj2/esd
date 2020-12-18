package com.esd.controller.pagecontrollers.errors;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;
import org.apache.http.HttpStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/error")
@Authentication(authenticationRequired = false)
public class ErrorController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        int errorCode = -1;
        try {
            errorCode = Integer.parseInt(request.getParameter("errorCode"));
        }catch (Exception e){

        }
        String contentTitle = "";
        String contentBody = "";
        String homeLinkText = "";
        String homeLink = "";
        if(request.getSession().getAttribute("currentSessionUser") != null){
            homeLinkText = "DashBoard";
            homeLink = UrlUtils.absoluteUrl(request, "dashboard");
        }else{
            homeLinkText = "Login";
            homeLink = UrlUtils.absoluteUrl(request, "login");
        }
        
        switch (errorCode){
            case HttpServletResponse.SC_UNAUTHORIZED:
                contentTitle = "Sorry! You're not allowed to see this page...";
                contentBody = "Please try logging in with your authorised account.<br>If this error persists, please contact your admin";
                break;
            case -1:
                contentTitle = "Sorry! something went wrong...";
                contentBody = "If this error persists, please contact your admin";
            default:
                break;
        }
        request.setAttribute("title", "Error " + errorCode);
        request.setAttribute("contentTitle", contentTitle);
        request.setAttribute("contentBody", contentBody);
        request.setAttribute("homeLinkText", homeLinkText);
        request.setAttribute("homeLink", homeLink);

        RequestDispatcher view = request.getRequestDispatcher("error.jsp");
        view.forward(request, response);
    }
}