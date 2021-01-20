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

import javax.servlet.annotation.WebServlet;
import java.util.*;

/**
 * Original Author: Trent meier
 * Use: the appointment search controller provides appointment views for a user
 * user search page
 */

@WebServlet("/appointments/schedule")
@Authentication(userGroups = {UserGroup.ALL})
public class AppointmentsSearchController extends GenericSearchController {

    private InvoiceService invoiceService = InvoiceService.getInstance();

    public AppointmentsSearchController() {
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
        List<Appointment> invoices = AppointmentsService.getInstance().getAppointmentsByFilteredResults(args);
        List<SearchRow> searchRows = new ArrayList<>();
        for(Appointment appointment : invoices){
            searchRows.add(new AppointmentSearchRow(appointment));
        }
        return new ArrayList<>();
    }
}
