package com.esd.controller.systemsettings;

import java.io.IOException;

import com.esd.model.systemsettings.SystemSettings;

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
public class SystemSettingsController extends HttpServlet {

	private static final String SUCCESS_ATTRIBUTE = "Success!";

	private static final String FAILURE_NOTIFY = "Save failed.";

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

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		SystemSettings sysSettings = SystemSettings.getInstance();
		sysSettings.updatePropertyWithoutSave(request.getParameter("baseConsultationFeeDoctor"));
		sysSettings.updatePropertyWithoutSave(request.getParameter("baseConsultationFeeNurse"));
		sysSettings.updatePropertyWithoutSave(request.getParameter("consultationSlotTimeMins"));
		sysSettings.save();

		request.setAttribute("success", SUCCESS_ATTRIBUTE);
		request.setAttribute("failure", FAILURE_NOTIFY);
		RequestDispatcher view = request.getRequestDispatcher("/systemSettings");
		response.setContentType("text/html;charset=UTF-8");
		view.forward(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Description";
	}
}
