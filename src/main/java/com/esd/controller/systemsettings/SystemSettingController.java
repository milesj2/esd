package com.esd.controller.systemsettings;

import com.esd.model.dao.DaoConsts;
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

/**
 * Original Author: Sam Barba
 * Use: The system settings controller use is to update the site settings as per a qualified user's request.
 */
@WebServlet("/admin/settings")
public class SystemSettingController extends HttpServlet {

	private SystemSettingService sysSettingService = SystemSettingService.getInstance();
	private final String SUCCESS_MESSAGE = "Settings successfully updated.";
	private final String INCORRECT_VALUE_MESSAGE = "Invalid value entered. Enter decimals for fees, or integer for slot time.";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			response.setContentType("text/html;charset=UTF-8");
			populateForm(request, response);
			RequestDispatcher view = request.getRequestDispatcher("/admin/settings.jsp");
			view.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			processRequest(request, response);
			populateForm(request, response);
			RequestDispatcher view = request.getRequestDispatcher("/admin/settings.jsp");
			view.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	private void populateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InvalidIdValueException, SQLException {
			request.setAttribute(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR, sysSettingService.getDoubleSettingValueByKey(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR));
			request.setAttribute(SystemSettingService.SYSTEMSETTING_FEE_NURSE, sysSettingService.getDoubleSettingValueByKey(SystemSettingService.SYSTEMSETTING_FEE_NURSE));
			request.setAttribute(SystemSettingService.SYSTEMSETTING_SLOT_TIME, sysSettingService.getIntegerSettingValueByKey(SystemSettingService.SYSTEMSETTING_SLOT_TIME));
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		String notification = "Error updating value(s) :(";

		try {
			if (sysSettingService.updateSystemSettingDouble(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR, request.getParameter(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR))
					|| sysSettingService.updateSystemSettingDouble(SystemSettingService.SYSTEMSETTING_FEE_NURSE, request.getParameter(SystemSettingService.SYSTEMSETTING_FEE_NURSE))
					|| sysSettingService.updateSystemSettingInteger(SystemSettingService.SYSTEMSETTING_SLOT_TIME, request.getParameter(SystemSettingService.SYSTEMSETTING_SLOT_TIME))) {
				notification = SUCCESS_MESSAGE;
			} else {
				notification = INCORRECT_VALUE_MESSAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("notification", notification);
	}
}
