package com.esd.controller.pagecontrollers.appointments;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.controller.pagecontrollers.search.SearchColumn;
import com.esd.controller.pagecontrollers.search.searchrow.AppointmentSearchRow;
import com.esd.controller.pagecontrollers.search.searchrow.SearchRow;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.service.AppointmentsService;

import com.esd.model.service.InvoiceService;
import org.joda.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Original Author: Trent meier
 * Use: the appointment search controller provides appointment views for a user
 * user search page
 */

@WebServlet("/appointments/search")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentSearchController extends GenericSearchController {

    private AppointmentsService appointmentsService = AppointmentsService.getInstance();
    private static final List<String> AppointmentKeys = new ArrayList<>(Arrays.asList(
            DaoConsts.APPOINTMENT_SLOTS,
            DaoConsts.APPOINTMENT_STATUS,
            DaoConsts.ID));

    private boolean checkRequestContains(HttpServletRequest request, String key){
        return request.getParameterMap().containsKey(key)
                && !request.getParameter(key).isEmpty()
                && request.getParameter(key) != "";
    }

    private InvoiceService invoiceService = InvoiceService.getInstance();

    public AppointmentSearchController() {
        columns = Arrays.asList(
                new SearchColumn(DaoConsts.ID, "Id", "number"),
                new SearchColumn(DaoConsts.INVOICE_DATE, "Invoice Date", "date"),
                new SearchColumn(DaoConsts.INVOICE_STATUS, "Status", "text"),
                new SearchColumn(DaoConsts.INVOICE_TIME, "Invoice time", "time"),
                new SearchColumn(DaoConsts.EMPLOYEE_ID, "Employee Id", "number"),
                new SearchColumn(DaoConsts.PATIENT_ID, "Patient Id", "number")
        );

        selectedKey = "selectedInvoiceId";
    }

    @Override

    public List<SearchRow> getSearchResults(SystemUser currentUser, Map<String, Object> args) {
        List<Appointment> invoices = AppointmentsService.getInstance().getAppointmentsByFilteredResults(currentUser, args);
        List<SearchRow> searchRows = new ArrayList<>();
        for (Appointment appointment : invoices) {
            searchRows.add(new AppointmentSearchRow(appointment));
        }
        return new ArrayList<>();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
            Map<String, Object> args =  new HashMap<>();
            for(String key: AppointmentKeys) {
                if(checkRequestContains(request, key)){
                    args.put(key, request.getParameter(key));
                }
            }

            try {
                // default of today todo filter for user

                LocalDate fromDate = checkRequestContains(request, "fromDate")
                        ? LocalDate.parse(request.getParameter("fromDate"))
                        : LocalDate.now();

                LocalDate toDate = checkRequestContains(request, "toDate")
                        ? LocalDate.parse(request.getParameter("toDate"))
                        : LocalDate.now();

                List<Appointment> appointmentList = appointmentsService.getAppointmentsInRange(fromDate, toDate, Optional.ofNullable(args));
                request.setAttribute("table", appointmentList);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/appointments/scheduleAppointment.jsp");
            requestDispatcher.forward(request, response);
    }
}
