/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esd.controller.authentication;


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
        

        int flag1 = 0;
        int flag2 = 0;
        String notify;
        String userId = "";
        String data = "('"+username.trim()+"',"+"'"+password.trim()+"',"+"'"+userGroup.trim()+"',"+active.trim()+")";
        UserDao db = UserDao.getInstance();
        boolean matchFound = db.verifyUsernameIsUnique(username);
        if (!matchFound) {
          flag1 = db.addUser2SystemUser(data);
          userId =  db.getUserId(username);
          String data2 = "("+userId.trim()+","+"'"+firstName.trim()+"',"+"'"+lastName.trim()+"',"+"'"+addressLine1.trim()+"',"+"'"+addressLine2.trim()+"',"+"'"+addressLine3.trim()+"',"+"'"+town.trim()+"',"+"'"+postCode.trim()+"',"+"'"+dob.trim()+"')";
          flag2 = db.addUser2UserDetails(data2);
          notify = "Sucessfully Registered! Please Sign in with the link below.";
        }
        else {
          notify = "Error: Username already exsists, please choose another username or sign in with the exsisting username";
        }
        int flag = flag1+flag2;
 
        request.setAttribute("notify",notify);
        request.setAttribute("msg",flag+" database tables updated");
        request.setAttribute("msg2",userId.trim()+" user id for user");
        RequestDispatcher view = request.getRequestDispatcher("/signup.jsp");
        view.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
