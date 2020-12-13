package com.esd.controller.systemsettings;

import java.io.IOException;

import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.SystemSetting;
import com.esd.model.data.persisted.User;
import com.esd.model.service.SystemSettingService;

import javax.servlet.RequestDispatcher;
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

	private static final String SUCCESS = "success";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// Validate user is logged in
		User currentUser = (User)(request.getSession().getAttribute("currentSessionUser"));
		if(currentUser == null){
			response.sendRedirect("../../index.jsp");
			return;
		} else if (currentUser.getUserGroup() != UserGroup.ADMIN){
			response.sendRedirect("../../index.jsp");
			return;
		}

		try {
			response.sendRedirect("systemSettings/systemSettings.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("index.jsp?err=true"); //error page
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		try {
			SystemSettingService sysSettingService = SystemSettingService.getInstance();

			SystemSetting feeDoctorSysSetting = new SystemSetting(DaoConsts.SYSTEMSETTING_FEE_DOCTOR, request.getParameter(DaoConsts.SYSTEMSETTING_FEE_DOCTOR));
			SystemSetting feeNurseSysSetting = new SystemSetting(DaoConsts.SYSTEMSETTING_FEE_DOCTOR, request.getParameter(DaoConsts.SYSTEMSETTING_FEE_NURSE));
			SystemSetting slotTimeSysSetting = new SystemSetting(DaoConsts.SYSTEMSETTING_SLOT_TIME, request.getParameter(DaoConsts.SYSTEMSETTING_SLOT_TIME));

			sysSettingService.updateSystemSetting(feeDoctorSysSetting);
			sysSettingService.updateSystemSetting(feeNurseSysSetting);
			sysSettingService.updateSystemSetting(slotTimeSysSetting);

			request.setAttribute(SUCCESS, SUCCESS);
			RequestDispatcher view = request.getRequestDispatcher("/systemSettings");
			response.setContentType("text/html;charset=UTF-8");
			view.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("index.jsp?err=true"); //error page
		}
	}

	@Override
	public String getServletInfo() {
		return "Description";
	}
}
