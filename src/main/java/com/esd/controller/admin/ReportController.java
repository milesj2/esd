package com.esd.controller.admin;

import com.esd.model.reportgen.SystemReports;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/reports")
public class ReportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher view = req.getRequestDispatcher("/admin/adminreports.jsp");
            view.forward(req,resp);
        }catch (ServletException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        try {
            String reportTypeKey = req.getParameter("report");

            String generatedReport = SystemReports.availableReports.get(reportTypeKey).generateReport(req.getParameterMap());
            req.setAttribute("generatedReport", generatedReport);
            req.setAttribute("backLink", req.getHeader("referer"));

            RequestDispatcher view = req.getRequestDispatcher("/admin/viewReport.jsp");
            view.forward(req,resp);
        }catch (ServletException | IOException e){
            e.printStackTrace();
        }

    }
}
