package com.esd.controller.pagecontrollers.admin;

import com.esd.controller.annotations.Authentication;
import com.esd.model.data.UserGroup;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.SystemSettingService;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Original Author: Sam Barba
 * Use: The system settings controller use is to update the site settings as per a qualified user's request.
 */
@WebServlet("/admin/settings")
@Authentication(userGroups = {UserGroup.ADMIN})
public class SystemSettingController extends HttpServlet {

    private SystemSettingService sysSettingService = SystemSettingService.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		session.setAttribute("previousPage", session.getAttribute("currentPage"));
		session.setAttribute("currentPage", request.getServletPath());

		try {
			response.setContentType("text/html;charset=UTF-8");
			populateForm(request);
			RequestDispatcher view = request.getRequestDispatcher("/admin/systemSettings.jsp");
			view.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			response.setContentType("text/html;charset=UTF-8");
			processRequest(request);
			populateForm(request);
			RequestDispatcher view = request.getRequestDispatcher("/admin/systemSettings.jsp");
			view.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void populateForm(HttpServletRequest request) throws InvalidIdValueException, SQLException {
			request.setAttribute(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR, sysSettingService.getDoubleSettingValueByKey(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR));
			request.setAttribute(SystemSettingService.SYSTEMSETTING_FEE_NURSE, sysSettingService.getDoubleSettingValueByKey(SystemSettingService.SYSTEMSETTING_FEE_NURSE));
			request.setAttribute(SystemSettingService.SYSTEMSETTING_SLOT_TIME, sysSettingService.getIntegerSettingValueByKey(SystemSettingService.SYSTEMSETTING_SLOT_TIME));
	}

	private void processRequest(HttpServletRequest request) {
		String notification = "";

		try {
			boolean feeDoctorUpdated = sysSettingService.updateSystemSettingDouble(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR, request.getParameter(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR));
			boolean feeNurseUpdated = sysSettingService.updateSystemSettingDouble(SystemSettingService.SYSTEMSETTING_FEE_NURSE, request.getParameter(SystemSettingService.SYSTEMSETTING_FEE_NURSE));
			boolean slotTimeUpdated = sysSettingService.updateSystemSettingInteger(SystemSettingService.SYSTEMSETTING_SLOT_TIME, request.getParameter(SystemSettingService.SYSTEMSETTING_SLOT_TIME));

			if (feeDoctorUpdated || feeNurseUpdated || slotTimeUpdated) {
                notification += "<p>Successfully updated setting(s).";
            }
			if (!feeDoctorUpdated) {
				notification += "<p>Failed to update doctor fee. Please use decimals.";
			}
			if (!feeNurseUpdated) {
				notification += "<p>Failed to update nurse fee. Please use decimals.";
			}
			if (!slotTimeUpdated) {
				notification += "<p>Failed to update slot time. Please use integers.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			notification = "<p>Error updating value(s) :(";
		}

		request.setAttribute("notification", notification);
	}
}
