package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.dao.SystemSettingDao;
import com.esd.model.dao.SystemUserDao;
import com.esd.model.data.InvoiceOptions;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.InvoiceService;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Original Author: Trent meier
 * Use: the user search controller validates the user and then redirects to the user search
 * page
 */

@WebServlet("/invoices/edit")
@Authentication(userGroups = {UserGroup.ALL})
public class InvoiceController extends HttpServlet {

    private InvoiceService invoiceService = InvoiceService.getInstance();
    private SystemSettingDao systemSettingDao = SystemSettingDao.getInstance();
    private String appointmentError = "Appointment employee must be doctor or nurse!";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!request.getParameter(DaoConsts.ID).isEmpty()){

            HttpSession session = request.getSession();
            session.setAttribute("previousPage", session.getAttribute("currentPage"));
            session.setAttribute("currentPage", request.getServletPath());

            try{
                Invoice invoice = invoiceService.getInvoiceById(Integer.parseInt(request.getParameter(DaoConsts.ID)));
                request.setAttribute("invoice", invoice);
            } catch (Exception e){
                request.setAttribute("message", "could not find invoice");
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/invoices/editInvoice.jsp");
            requestDispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idVal = Integer.parseInt(request.getParameter(DaoConsts.ID));

            Invoice invoice = new Invoice();
            invoice.setInvoiceDate(LocalDate.parse(request.getParameter(DaoConsts.INVOICE_DATE)));
            invoice.setInvoiceTime(LocalTime.parse(request.getParameter(DaoConsts.INVOICE_TIME)));
            invoice.setInvoiceStatus(InvoiceStatus.valueOf(request.getParameter(DaoConsts.INVOICE_STATUS)));
            invoice.setId(Integer.parseInt(request.getParameter(DaoConsts.ID)));
            invoice.setInvoiceStatusChangeDate(LocalDate.now());
            invoice.setEmployeeId(Integer.parseInt(request.getParameter(DaoConsts.EMPLOYEE_ID)));
            invoice.setPatientId(Integer.parseInt(request.getParameter(DaoConsts.PATIENT_ID)));
            invoice.setPrivatePatient(request.getParameter(DaoConsts.PRIVATE_PATIENT)==null);
            invoice.setAppointmentId(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_ID_FK)));

            List<InvoiceItem> invoiceItems = invoiceService.getInvoiceById(invoice.getId()).getItems();
            invoiceItems.get(0).setQuantity(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_SLOTS)));
            invoiceItems.get(0).setDescription("Invoice Item: Appointment on "+ invoice.getInvoiceDate().toString());

            SystemUser employeeUser = SystemUserDao.getInstance().getUserByID(invoice.getEmployeeId());
            if(employeeUser.getUserGroup() == UserGroup.DOCTOR){
                invoiceItems.get(0).setCost(systemSettingDao.getDoubleSettingValueByKey("baseConsultationFeeDoctor"));
            } else if(employeeUser.getUserGroup() == UserGroup.NURSE){
                invoiceItems.get(0).setCost(systemSettingDao.getDoubleSettingValueByKey("baseConsultationFeeNurse"));
            } else {
                request.setAttribute("message", appointmentError);
                throw new InvalidIdValueException(appointmentError);
            }
            invoice.setItems(invoiceItems);

            //update
            invoiceService.updateInvoice(invoice);

            request.setAttribute("message", "Success");
            Invoice updatedInvoice = invoiceService.getInvoiceById(idVal);
            request.setAttribute("invoice", updatedInvoice);

        } catch (Exception e){
            request.setAttribute("message", "Something went wrong. If this persists please contact your administrator");
            e.printStackTrace();
        }

        // dispatch
        RequestDispatcher view = request.getRequestDispatcher("/invoices/editInvoice.jsp");
        view.forward(request, response);
    }
}
