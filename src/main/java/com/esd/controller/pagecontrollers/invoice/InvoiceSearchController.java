package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.User;
import com.esd.model.service.InvoiceService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Original Author: Trent meier
 * Use: the invoice search controller provides invoice access filtering and redirects to
 * user search page
 */
@WebServlet("/invoices/search")
@Authentication(userGroups = {UserGroup.ALL})
public class InvoiceSearchController extends GenericSearchController {

    private InvoiceService invoiceService = InvoiceService.getInstance();

    public InvoiceSearchController() {
        formValues =  new ArrayList<>(Arrays.asList(
                DaoConsts.ID,
                DaoConsts.INVOICE_DATE,
                DaoConsts.INVOICE_STATUS,
                DaoConsts.INVOICE_TIME,
                DaoConsts.INVOICE_STATUS,
                DaoConsts.EMPLOYEE_ID,
                DaoConsts.PATIENT_ID,
                DaoConsts.ID));

        searchFilterFunction = invoiceService::getInvoiceFromFilteredRequest;
        searchPage = "/invoices/searchInvoices.jsp";
        selectedKey = "selectedInvoiceId";
    }
}
