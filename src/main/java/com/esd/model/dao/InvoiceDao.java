package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.Invoice;

import javax.servlet.http.HttpServletRequest;

import com.esd.model.data.persisted.InvoiceItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to invoices
 */
public class InvoiceDao {

    private static InvoiceDao instance;
    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String MATCH = " = ?";

    private static final String GET_FILTERED_INVOICES = "SELECT * FROM INVOICE";

    private InvoiceDao() {}

    public List<Invoice> getAllInvoicesWithStatus(Date start, Date end, Optional<InvoiceStatus> status, boolean loadItems) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE)
                .withRestriction(Restrictions.greaterThanInclusive(DaoConsts.INVOICE_DATE, start))
                .withRestriction(Restrictions.lessThanInclusive(DaoConsts.INVOICE_DATE, end));

        if(status.isPresent()){
            queryBuilder.withRestriction(Restrictions.equalsRestriction(DaoConsts.INVOICE_STATUS, status.get().name()));
        }
        return processResultSetForInvoices(loadItems, queryBuilder.createStatement());
    }

    public List<Invoice> getInvoiceWithStatusChangeToThisPeriod(Date start, Date end, InvoiceStatus status,boolean loadItems) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE)
                .withRestriction(Restrictions.greaterThanInclusive(DaoConsts.INVOICE_DATE, start))
                .withRestriction(Restrictions.lessThanInclusive(DaoConsts.INVOICE_DATE, end))
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.INVOICE_STATUS, status.toString()));

        return processResultSetForInvoices(loadItems, queryBuilder.createStatement());
    }

    public List<Invoice> getAllInvoicesWithStatus(InvoiceStatus status, boolean loadItems) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.INVOICE_STATUS, status.toString()));

        return processResultSetForInvoices(loadItems, queryBuilder.createStatement());
    }

    private List<Invoice> processResultSetForInvoices(boolean loadItems, PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        List<Invoice> invoices = new ArrayList<>();
        while (result.next()){
            Invoice invoice = extractInvoiceFromResultSet(result);
            if(loadItems){
                invoice.setItems(getAllInvoiceItemsForInvoiceId(invoice.getId()));
            }else{
                invoice.setItems(new ArrayList<>());
            }
            invoices.add(invoice);
        }
        return invoices;
    }

    public List<InvoiceItem> getAllInvoiceItemsForInvoiceId(int id) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICEITEM)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, id));

        PreparedStatement statement = queryBuilder.createStatement();

        ResultSet result = statement.executeQuery();
        List<InvoiceItem> allItems = new ArrayList<>();

        while (result.next()){
            allItems.add(extractInvoiceItemFromResultSet(result));
        }
        return  allItems;
    }

    public Invoice extractInvoiceFromResultSet(ResultSet resultSet) throws SQLException {
        Invoice invoice =  new Invoice();
        invoice.setId(resultSet.getInt(DaoConsts.ID));
        invoice.setInvoiceDate(resultSet.getDate(DaoConsts.INVOICE_DATE));
        invoice.setInvoiceStatusChangeDate(resultSet.getDate(DaoConsts.INVOICE_STATUS_CHANGE_DATE));
        invoice.setInvoiceTime(resultSet.getTime(DaoConsts.INVOICE_TIME));
        invoice.setInvoiceStatus(InvoiceStatus.valueOf(InvoiceStatus.class,
                resultSet.getString(DaoConsts.INVOICE_STATUS).toUpperCase()));
        invoice.setEmployeeId(resultSet.getInt(DaoConsts.EMPLOYEE_ID_FK));
        invoice.setPatientId(resultSet.getInt(DaoConsts.PATIENT_ID_FK));
        invoice.setPrivatePatient(resultSet.getBoolean(DaoConsts.PRIVATE_PATIENT));
        invoice.setAppointmentId(resultSet.getInt(DaoConsts.APPOINTMENT_ID_FK));

        return invoice;
    }

    private InvoiceItem extractInvoiceItemFromResultSet(ResultSet resultSet) throws SQLException {
        InvoiceItem invoiceItem =  new InvoiceItem();
        invoiceItem.setId(resultSet.getInt(DaoConsts.ID));
        invoiceItem.setInvoiceId(resultSet.getInt(DaoConsts.INVOICE_ID_FK));
        invoiceItem.setCost(resultSet.getDouble(DaoConsts.INVOICEITEM_COST));
        invoiceItem.setQuantity(resultSet.getInt(DaoConsts.INVOICEITEM_QUANTITY));
        invoiceItem.setDescription(resultSet.getString(DaoConsts.INVOICEITEM_DESCRIPTION));
        return invoiceItem;
    }

    public List<Invoice> getFilteredDetails(Map<String, Object> args) throws SQLException {
        ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE);

        Iterator mapIter = args.entrySet().iterator();
        while(mapIter.hasNext()) {
            Map.Entry pair = (Map.Entry)mapIter.next();
            queryBuilder.and(Restrictions.equalsRestriction(pair.getKey().toString(), pair.getValue()));
        }

        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet result = statement.executeQuery();

        while (result.next()){
            invoiceList.add(extractInvoiceFromResultSet(result));
        }
        return invoiceList;
    }

    public synchronized static InvoiceDao getInstance(){
        if(instance == null){
            instance = new InvoiceDao();
        }
        return instance;
    }
}

