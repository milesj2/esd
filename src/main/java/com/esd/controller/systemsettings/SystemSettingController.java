package com.esd.controller.systemsettings;

import java.io.IOException;

import com.esd.model.dao.DaoConsts;
import com.esd.model.service.SystemSettingService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Original Author: Sam Barba
 * 
 * Use: The system settings controller use is to update the site settings as per a qualified user's request. Settings
 * are persisted via a conf file.
 *
 */
@WebServlet("/systemSettings")
public class SystemSettingController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		populateForm(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		processRequest(request, response);
	}

	private void populateForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		SystemSettingService sysSettingService = SystemSettingService.getInstance();

		try {
			request.setAttribute(DaoConsts.SYSTEMSETTING_FEE_DOCTOR, sysSettingService.getDoubleSettingValueByKey(DaoConsts.SYSTEMSETTING_FEE_DOCTOR));
			request.setAttribute(DaoConsts.SYSTEMSETTING_FEE_NURSE, sysSettingService.getDoubleSettingValueByKey(DaoConsts.SYSTEMSETTING_FEE_NURSE));
			request.setAttribute(DaoConsts.SYSTEMSETTING_SLOT_TIME, sysSettingService.getIntegerSettingValueByKey(DaoConsts.SYSTEMSETTING_SLOT_TIME));
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("index.jsp?err=true"); //error page
		}

		RequestDispatcher view = request.getRequestDispatcher("/systemSettings/systemSettings.jsp");
		view.forward(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8");
		String notification = "";
		SystemSettingService sysSettingService = SystemSettingService.getInstance();

		try {
			if (sysSettingService.updateSystemSetting(DaoConsts.SYSTEMSETTING_FEE_DOCTOR, request.getParameter(DaoConsts.SYSTEMSETTING_FEE_DOCTOR))
					|| sysSettingService.updateSystemSetting(DaoConsts.SYSTEMSETTING_FEE_NURSE, request.getParameter(DaoConsts.SYSTEMSETTING_FEE_NURSE))
					|| sysSettingService.updateSystemSetting(DaoConsts.SYSTEMSETTING_SLOT_TIME, request.getParameter(DaoConsts.SYSTEMSETTING_SLOT_TIME))) {
				notification = "Settings successfully updated.";
			} else {
				notification = "Invalid value entered. Enter decimals for fees, or integer for slot time.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("notification", notification);
		RequestDispatcher view = request.getRequestDispatcher("/systemSettings/systemSettings.jsp");
		view.forward(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Description";
	}
}
