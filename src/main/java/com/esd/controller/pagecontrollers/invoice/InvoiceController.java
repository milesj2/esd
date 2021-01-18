package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.InvoiceOptions;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
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
import java.util.ArrayList;

/**
 * Original Author: Trent meier
 * Use: the user search controller validates the user and then redirects to the user search
 * page
 */

@WebServlet("/invoices/view")
@Authentication(userGroups = {UserGroup.ALL})
public class InvoiceController extends HttpServlet {

    private InvoiceService invoiceService = InvoiceService.getInstance();

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
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/invoices/viewInvoice.jsp");
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
            if(InvoiceOptions.valueOf(request.getParameter("option")) == InvoiceOptions.UPDATE){
                invoice.setId(Integer.parseInt(request.getParameter(DaoConsts.ID)));
                invoice.setInvoiceStatusChangeDate(LocalDate.now());
            } else {
                invoice.setId(0);
                invoice.setInvoiceStatusChangeDate(LocalDate.parse(request.getParameter(DaoConsts.INVOICE_STATUS_CHANGE_DATE)));
            }
            invoice.setEmployeeId(Integer.parseInt(request.getParameter(DaoConsts.EMPLOYEE_ID)));
            invoice.setPatientId(Integer.parseInt(request.getParameter(DaoConsts.PATIENT_ID)));
            invoice.setPrivatePatient(request.getParameter(DaoConsts.PRIVATE_PATIENT)=="true");
            invoice.setAppointmentId(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_ID_FK)));

            ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
            //derive invoice related attributes
            InvoiceItem invoiceItem = invoiceService.deriveInvoiceItemAttributes(invoice);
            //add additional attributes
            invoiceItem.setQuantity(Integer.parseInt(request.getParameter(DaoConsts.APPOINTMENT_SLOTS)));
            invoiceItem.setDescription("Invoice For: "+  invoice.getInvoiceDate().toString());


            if(InvoiceOptions.valueOf(request.getParameter("option")) == InvoiceOptions.UPDATE) {
                invoiceService.updateInvoice(invoice, invoiceItems);
            } else {
                invoiceService.createInvoice(invoice, invoiceItems);
            }

            request.setAttribute("message", "Success");
            invoice = invoiceService.getInvoiceById(idVal);
            request.setAttribute("appointment", invoice);

        } catch (Exception e){
            e.printStackTrace();
        }

        // dispatch
        RequestDispatcher view = request.getRequestDispatcher("/invoices/viewInvoice.jsp");
        view.forward(request, response);
    }
}
