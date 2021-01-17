package com.esd.controller.pagecontrollers.authentication;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.service.SystemUserService;

import javax.servlet.annotation.WebServlet;
import org.joda.time.LocalDate;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Original Author: Angela Jackson
 * Use: the register user search controller redirects to the sign up page
 */
@WebServlet("/register")
@Authentication(loggedInUserAccess=false, authenticationRequired = false)
public class RegisterUserController extends HttpServlet {

    private static final String REGISTER_SUCCESS = "Successfully Registered! Please Sign in with the link below.";
    private static final String USER_EXISTS_ERROR = "Error: Username already exists, please choose another username or sign in with the existing username";
    private static final String REGISTER_FAILURE = "Error: User creation failed, please try again. If problem persists contact admin.";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String notify = "";
        try {
            response.setContentType("text/html;charset=UTF-8");
            SystemUser systemUser = createUserFromRequest(request);
            UserDetails userDetails = createUserDetailsFromRequest(request);

            if (systemUser.getUserGroup() == UserGroup.NHS_PATIENT || systemUser.getUserGroup() == UserGroup.PRIVATE_PATIENT) {
                systemUser.setActive(true);
            }
            
            boolean userRegisterd = SystemUserService.getInstance().createUser(systemUser, userDetails);
            if (userRegisterd) {
                notify = REGISTER_SUCCESS;
            } else {
              notify = USER_EXISTS_ERROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            notify = REGISTER_FAILURE;
        }
      
        request.setAttribute("notify",notify);
        RequestDispatcher view = request.getRequestDispatcher("/register.jsp");
        view.forward(request, response);
    }

    private SystemUser createUserFromRequest(HttpServletRequest request) throws ParseException {
        SystemUser systemUser = new SystemUser();
        systemUser.setPassword(request.getParameter(DaoConsts.SYSTEMUSER_PASSWORD));
        systemUser.setUserGroup(UserGroup.valueOf(request.getParameter(DaoConsts.SYSTEMUSER_USERGROUP)));
        systemUser.setUsername(request.getParameter(DaoConsts.SYSTEMUSER_USERNAME));
        return systemUser;
    }

    private UserDetails createUserDetailsFromRequest(HttpServletRequest request) throws ParseException {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(request.getParameter(DaoConsts.USERDETAILS_FIRSTNAME));
        userDetails.setLastName(request.getParameter(DaoConsts.USERDETAILS_LASTNAME));

        userDetails.setDateOfBirth(LocalDate.parse(request.getParameter(DaoConsts.USERDETAILS_DOB)));
        userDetails.setAddressLine1(request.getParameter(DaoConsts.USERDETAILS_ADDRESS1));
        userDetails.setAddressLine2(request.getParameter(DaoConsts.USERDETAILS_ADDRESS2));
        userDetails.setAddressLine3(request.getParameter(DaoConsts.USERDETAILS_ADDRESS3));
        userDetails.setTown(request.getParameter(DaoConsts.USERDETAILS_TOWN));
        userDetails.setPostCode(request.getParameter(DaoConsts.USERDETAILS_POSTCODE));
        return userDetails;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher view = req.getRequestDispatcher("/register.jsp");
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
}
