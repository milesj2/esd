package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.service.InvoiceService;

import javax.servlet.annotation.WebServlet;
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
