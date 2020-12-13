package com.esd.controller.authentication;

import com.esd.model.data.UserGroup;
import javax.servlet.annotation.WebServlet;
import com.esd.model.service.UserService;
import com.esd.model.dao.UserDao;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Original Author: Angela Jackson
 * Use: the register user search controller redirects to the sign up page
 */
@WebServlet("/registerUser")
public class RegisterUser extends HttpServlet {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          String notify = "";
        try {
            response.setContentType("text/html;charset=UTF-8");
            
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            Date dob = dateFormatter.parse(request.getParameter("dob"));
            String userGroup = request.getParameter("userGroup");
            String addressLine1 = request.getParameter("addressLine1");
            String addressLine2 = request.getParameter("addressLine2");
            String addressLine3 = request.getParameter("addressLine3");
            String town = request.getParameter("town");
            String postCode = request.getParameter("postCode");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String active = "false";
            
            if (UserGroup.valueOf(userGroup) == UserGroup.NHS_PATIENT || UserGroup.valueOf(userGroup) == UserGroup.PRIVATE_PATIENT) {
                active = "true";
            }
            
            boolean userRegisterd = UserService.getInstance()
                    .createUser(username, password, active, userGroup, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob);
            if (userRegisterd) {
                notify = "Sucessfully Registered! Please Sign in with the link below.";
            }
            else {
                notify = "Error: Username already exsists, please choose another username or sign in with the exsisting username";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            notify = "Error: User creation failed, please try again. If problem persists contact admin.";
        }
      
        request.setAttribute("notify",notify);
        RequestDispatcher view = request.getRequestDispatcher("/registerUser.jsp");
        view.forward(request, response);//endtry
       
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher view = req.getRequestDispatcher("/registerUser.jsp");
            view.forward(req,resp);
        }catch (ServletException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
