package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.InvoiceOptions;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
import com.esd.model.data.persisted.User;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.InvoiceService;
import com.esd.model.service.UserDetailsService;
import com.esd.model.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Original Author: Angela Jackson
 * Use: the invoice payment controller validates the invoice and then redirects to the payment page.
 * page
 */

@WebServlet("/invoices/pay")
@Authentication(userGroups = {UserGroup.ALL})
public class InvoicePaymentController extends HttpServlet {

    private InvoiceService invoiceService = InvoiceService.getInstance();
    private UserService userService = UserService.getInstance();
    private UserDetailsService userDetailsService = UserDetailsService.getInstance();
    private AppointmentsService appointmentsService = AppointmentsService.getInstance();
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!request.getParameter(DaoConsts.ID).isEmpty()){           
            
            HttpSession session = request.getSession();
            session.setAttribute("previousPage", session.getAttribute("currentPage"));
            session.setAttribute("currentPage", request.getServletPath());

            try{
                Invoice invoice = invoiceService.getInvoiceById(Integer.parseInt(request.getParameter(DaoConsts.ID)));
                InvoiceItem invoiceItem = invoiceService.getInvoiceItemById(Integer.parseInt(request.getParameter(DaoConsts.ID)));
                
                User user = userService.getUserByID(Integer.parseInt(request.getParameter("uid")));
                user.setUserDetails(userDetailsService.getUserDetailsByUserID(user.getId()));
                
                User employee = userService.getUserByID(Integer.parseInt(request.getParameter("eid")));
                employee.setUserDetails(userDetailsService.getUserDetailsByUserID(employee.getId()));
                
                Appointment appointment = appointmentsService.getAppointmentById(Integer.parseInt(request.getParameter("aid")));
                
                request.setAttribute("invoice", invoice);
                request.setAttribute("invoiceItem", invoiceItem);
                request.setAttribute("user", user);
                request.setAttribute("employee", employee);
                request.setAttribute("appointment", appointment);
            } catch (Exception e){
                request.setAttribute("message", "could not find invoice");
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/invoices/payInvoice.jsp");
            requestDispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idVal = Integer.parseInt(request.getParameter(DaoConsts.ID));

            Invoice invoice = new Invoice();
            invoice.setInvoiceStatus(InvoiceStatus.PAID);
            invoiceService.updateInvoiceStatus(invoice);
            
            request.setAttribute("message", "Payment Success");
            invoice = invoiceService.getInvoiceById(idVal);
            request.setAttribute("appointment", invoice);

        } catch (Exception e){
            e.printStackTrace();
        }

        // dispatch
        RequestDispatcher view = request.getRequestDispatcher("/invoices/payInvoice.jsp");
        view.forward(request, response);
    }

}
