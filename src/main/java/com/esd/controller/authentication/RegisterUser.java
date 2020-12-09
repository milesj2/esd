/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esd.controller.authentication;

import javax.servlet.annotation.WebServlet;
import com.esd.model.service.UserService;
import com.esd.model.dao.UserDao;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author angela
 */
@WebServlet("/RegisterUser")
public class RegisterUser extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String dob = request.getParameter("dob");
        String userGroup = request.getParameter("userGroup");
        String addressLine1 = request.getParameter("addressLine1");
        String addressLine2 = request.getParameter("addressLine2");
        String addressLine3 = request.getParameter("addressLine3");
        String town = request.getParameter("town");
        String postCode = request.getParameter("postCode");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String active = "true";
        

        String notify = "";
        try {
            boolean userRegisterd = UserService.getInstance()
                .createUser(username, password, active, userGroup, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob);
            if (userRegisterd) {
              notify = "Sucessfully Registered! Please Sign in with the link below.";
            }
            else {
              notify = "Error: Username already exsists, please choose another username or sign in with the exsisting username";
            }
        }//endtry
        catch (Exception e) {
          System.out.println("Error: " + e);
        }
        
        request.setAttribute("notify",notify);
        RequestDispatcher view = request.getRequestDispatcher("/signup.jsp");
        view.forward(request, response);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
