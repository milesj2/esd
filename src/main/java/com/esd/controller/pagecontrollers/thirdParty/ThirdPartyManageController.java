package com.esd.controller.pagecontrollers.thirdParty;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.ThirdParty;
import com.esd.model.service.ThirdPartyService;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Original Author: Angela Jackson
 * Use: the third party manage controller is to show saved third parties and option to edit them.
 */
@WebServlet("/thirdPartyManagement/manage")
@Authentication(userGroups = {UserGroup.ADMIN})
public class ThirdPartyManageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        
        request.setAttribute("pageTitle", "Third Party Managment");
        List<ThirdParty> thirdParties;

        try {
            thirdParties = ThirdPartyService.getInstance().getThirdParties();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        request.setAttribute("thirdParties", thirdParties);
        RequestDispatcher view = request.getRequestDispatcher("/thirdPartyManagement/manageThirdParty.jsp");
        view.forward(request, response);
    }

}
