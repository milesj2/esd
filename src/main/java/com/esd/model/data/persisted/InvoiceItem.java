package com.esd.model.data.persisted;

import java.math.BigDecimal;

/**
 * Original Author: Jordan Hellier
 * Use: This class is a simple data class used to store the data about an invoice item
 */
public class InvoiceItem {
    private int id;
    private int invoiceId;
    private double cost;
    private int quantity;
    private String description;

    public double getTotalCost(){
        return cost * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
