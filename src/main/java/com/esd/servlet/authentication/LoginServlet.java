package com.esd.servlet.authentication;

import com.esd.model.data.User;
import com.esd.model.data.UserGroup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        try
        {
            User user = login(request.getParameter("username"), request.getParameter("password"));

            if (user != null)
            {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser",user);
                response.sendRedirect("index.jsp"); //logged-in page
            }

            else
                response.sendRedirect("index.jsp?err=true"); //error page
        }
        catch (Throwable theException)
        {
            System.out.println(theException);
            throw theException;
        }
    }

    private User login(String username, String password){
        if(username.equals("patient") && password.equals("patient")){
            return new User(username, password, UserGroup.PATIENT);
        }
        return null;
    }

}
