package com.esd.controller.pagecontrollers.thirdParty;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.ThirdPartyType;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.ThirdParty;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.ThirdPartyService;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;


/**
 * Original Author: Angela Jackson
 * Use: the edit controller is to pass updated third parties details from editThirdParty.jsp post data in SQL.
 */
@WebServlet("/thirdPartyManagement/edit")
@Authentication(userGroups = {UserGroup.ADMIN})
public class ThirdPartyEditController extends HttpServlet {

    private ThirdPartyService thirdPartyService = ThirdPartyService.getInstance();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        request.setAttribute("pageTitle", "Edit Third Party");

        ThirdParty thirdParty;

        try {
            thirdParty = thirdPartyService.getThirdPartyByID(Integer.parseInt(request.getParameter("id")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        } catch (InvalidIdValueException | NumberFormatException e){
            response.sendRedirect("manage?errMsg=" + e.getMessage());
            return;
        }
        request.setAttribute("editThirdParty", thirdParty);
        RequestDispatcher view = request.getRequestDispatcher("/thirdPartyManagement/editThirdParty.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        boolean active = false;
        if (request.getParameter(DaoConsts.THIRDPARTY_ACTIVE) != null) {
            active = true;
        }

        ThirdParty thirdParty = new ThirdParty(
                Integer.parseInt(request.getParameter(DaoConsts.THIRDPARTY_ID)),
                request.getParameter(DaoConsts.THIRDPARTY_NAME),
                request.getParameter(DaoConsts.THIRDPARTY_ADDRESS1),
                request.getParameter(DaoConsts.THIRDPARTY_ADDRESS2),
                request.getParameter(DaoConsts.THIRDPARTY_ADDRESS3),
                request.getParameter(DaoConsts.THIRDPARTY_TOWN),
                request.getParameter(DaoConsts.THIRDPARTY_POSTCODE),
                request.getParameter(DaoConsts.THIRDPARTY_TYPE),
                active
        );


        // Try updating SQL
        try {
            boolean updateThirdPartyResult;

            updateThirdPartyResult = thirdPartyService.updateThirdParty(thirdParty);

            if (updateThirdPartyResult) {
                response.sendRedirect("manage?errMsg=Success");
            } else {
                response.sendRedirect("manage?errMsg=Error updating third party details.");
            }

        } catch (SQLException e) {
            response.sendRedirect("manage?errMsg=" + e.getMessage());
            System.out.println(e.getMessage());
        } catch (InvalidIdValueException e) {
            response.sendRedirect("manage?errMsg=" + "Invalid id user'" + thirdParty.getId() + "'");
            System.out.println(e.getMessage());
        }
    }


}
