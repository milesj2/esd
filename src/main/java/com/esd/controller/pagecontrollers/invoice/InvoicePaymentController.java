package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.InvoiceOptions;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;
import com.esd.model.service.AppointmentsService;
import com.esd.model.service.InvoiceService;
import com.esd.model.service.UserDetailsService;
import com.esd.model.service.SystemUserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Original Author: Angela Jackson
 * Use: the invoice payment controller validates the invoice and then redirects to the payment page.
 * page
 */

@WebServlet("/invoices/pay")
@Authentication(userGroups = {UserGroup.ALL})
public class InvoicePaymentController extends HttpServlet {

    private InvoiceService invoiceService = InvoiceService.getInstance();
    private SystemUserService userService = SystemUserService.getInstance();
    private UserDetailsService userDetailsService = UserDetailsService.getInstance();
    private AppointmentsService appointmentsService = AppointmentsService.getInstance();
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("selectedInvoiceId") == null){
            response.sendRedirect(UrlUtils.absoluteUrl(request, "search")); //search page
        return;
        }
              
            
            HttpSession session = request.getSession();
            request.setAttribute("pageTitle", "Invoice Payment");
            session.setAttribute("previousPage", session.getAttribute("currentPage"));
            session.setAttribute("currentPage", request.getServletPath());

            try{
                Invoice invoice = invoiceService.getInvoiceById(Integer.parseInt(request.getParameter("selectedInvoiceId")));
                invoice.setItems(invoiceService.getAllInvoiceItemsForInvoiceId(invoice.getId()));
                
                SystemUser user = (SystemUser)request.getSession().getAttribute("currentSessionUser");
                
                if(Arrays.asList(UserGroup.NHS_PATIENT, UserGroup.PRIVATE_PATIENT).contains(user.getUserGroup())){
                    UserDetails details = userDetailsService.getInstance().getUserDetailsByUserID(user.getId());
                    if(details.getId() != invoice.getPatientId()){
                        response.sendRedirect(UrlUtils.absoluteUrl(request, "dashboard")); //logged-in page
                        System.out.println("Return Error");
                        return;
                    }
                }
                
                //total the cost of the whole invoice
                double total = 0.0d;
                for(InvoiceItem item : invoice.getItems()){
                    total += item.getTotalCost(); 
                }
                
                request.setAttribute("totalDue", total);
               
                SystemUser patient = userService.getUserByUserDetailsId(invoice.getPatientId());
                patient.setUserDetails(userDetailsService.getUserDetailsByUserID(patient.getId()));
                
                SystemUser employee = userService.getUserByUserDetailsId(invoice.getEmployeeId());
                employee.setUserDetails(userDetailsService.getUserDetailsByUserID(employee.getId()));
                
                request.setAttribute("invoice", invoice);
                request.setAttribute("patient", patient);
                request.setAttribute("employee", employee);
                
            } catch (Exception e){
                e.printStackTrace();
                request.setAttribute("message", "could not find invoice");
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/invoices/payInvoice.jsp");
            requestDispatcher.forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter(DaoConsts.ID));
            String status = (InvoiceStatus.PAID).name();
            
            invoiceService.updateInvoiceStatus(id, status);
            
            response.sendRedirect("search?msg=" + "Payment Successful");

        } catch (SQLException e) {
            response.sendRedirect("search?msg=" + e.getMessage());
            System.out.println(e.getMessage());
        } catch (InvalidIdValueException e) {
            response.sendRedirect("search?msg=" + "Invalid invoice Id");
            System.out.println(e.getMessage());
        }
    }

}
