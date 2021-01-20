package com.esd.controller.pagecontrollers.invoice;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.pagecontrollers.GenericSearchController;
import com.esd.controller.pagecontrollers.search.SearchColumn;
import com.esd.controller.pagecontrollers.search.searchrow.InvoiceSearchRow;
import com.esd.controller.pagecontrollers.search.searchrow.SearchRow;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.SystemUser;
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
        List<Invoice> invoices = InvoiceService.getInstance().getInvoiceFromFilteredRequest(args);
        List<SearchRow> searchRows = new ArrayList<>();
        for(Invoice invoice : invoices){
            searchRows.add(new InvoiceSearchRow(invoice));
        }
        return searchRows;
    }

}
