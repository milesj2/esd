package com.esd.model.data;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple Enum for holding the status of the invoice, it can allow us to do special logic later
 * and makes a nice implementation for reports.
 */
public enum InvoiceStatus {
    PAID,
    UNPAID,
    OVERDUE
}
