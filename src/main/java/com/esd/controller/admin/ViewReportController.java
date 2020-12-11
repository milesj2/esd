package com.esd.controller.admin;

import com.esd.model.reportgen.SystemReports;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/viewReport")
public class ViewReportController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportTypeKey = req.getParameter("report");

        String generatedReport = SystemReports.availableReports.get(reportTypeKey).generateReport(req.getParameterMap());
        req.setAttribute("generatedReport", generatedReport);
        req.setAttribute("backLink", req.getHeader("referer"));

        RequestDispatcher view = req.getRequestDispatcher("/admin/viewReport.jsp");
        view.forward(req,resp);
    }
}
