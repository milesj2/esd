package com.esd.controller.pagecontrollers.search.searchrow;

import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.UserDetails;

import java.util.Arrays;
import java.util.HashMap;

public class InvoiceSearchRow extends SearchRow{

    public InvoiceSearchRow(Invoice invoice) {
            this.id = invoice.getId();
            this.columns = Arrays.asList(
                    String.valueOf(invoice.getId()),
                    invoice.getInvoiceDate().toString(),
                    invoice.getInvoiceStatus().toString(),
                    invoice.getInvoiceTime().toString(),
                    String.valueOf(invoice.getEmployeeId()),
                    String.valueOf(invoice.getPatientId())
            );
            searchActions = new HashMap<>();
            searchActions.put("/invoices/view?selectedInvoiceId="+invoice.getId(), "View Invoice");
            searchActions.put("/invoices/pay?selectedInvoiceId="+invoice.getId(), "Pay invoice");
    }
}
