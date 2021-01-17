package com.esd.controller.pagecontrollers.thirdParty;

import com.esd.model.dao.DaoConsts;
import com.esd.controller.annotations.Authentication;
import com.esd.model.data.ThirdPartyType;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.ThirdParty;
import com.esd.model.service.ThirdPartyService;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Original Author: Angela Jackson
 * Use: the third party controller redirects to the third party management page
 */
@WebServlet("/thirdPartyManagement/add")
@Authentication(userGroups = {UserGroup.ADMIN})
public class ThirdPartyController extends HttpServlet {
    
    private static final String REQUEST_SUCCESS = "Successfully Added!";
    private static final String REQUEST_FAILURE = "Error: Third party creation failed, please try again. If problem persists contact admin.";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String notify = "";
        try {
            response.setContentType("text/html;charset=UTF-8");
            ThirdParty thirdParty = createThirdPartyFromRequest(request);
            thirdParty.setActive(true);

            boolean thirdPartyCreated = ThirdPartyService.getInstance().createThirdParty(thirdParty);
            if (thirdPartyCreated) {
                notify = REQUEST_SUCCESS;
            } else {
              notify = REQUEST_FAILURE;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            notify = REQUEST_FAILURE;
        }
        
        request.setAttribute("notify",notify);
        RequestDispatcher view = request.getRequestDispatcher("/thirdPartyManagement/addThirdParty.jsp");
        view.forward(request, response);
    }
    
    private ThirdParty createThirdPartyFromRequest(HttpServletRequest request) throws ParseException {
        ThirdParty thirdParty = new ThirdParty();
        thirdParty.setName(request.getParameter(DaoConsts.THIRDPARTY_NAME));
        thirdParty.setAddressLine1(request.getParameter(DaoConsts.THIRDPARTY_ADDRESS1));
        thirdParty.setAddressLine2(request.getParameter(DaoConsts.THIRDPARTY_ADDRESS2));
        thirdParty.setAddressLine3(request.getParameter(DaoConsts.THIRDPARTY_ADDRESS3));
        thirdParty.setTown(request.getParameter(DaoConsts.THIRDPARTY_TOWN));
        thirdParty.setPostCode(request.getParameter(DaoConsts.THIRDPARTY_POSTCODE));
        thirdParty.setType(request.getParameter(DaoConsts.THIRDPARTY_TYPE));
        return thirdParty;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setAttribute("pageTitle", "Add Third Party");
            RequestDispatcher view = req.getRequestDispatcher("/thirdPartyManagement/addThirdParty.jsp");
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
    }

}
