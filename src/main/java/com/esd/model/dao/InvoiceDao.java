package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.persisted.Invoice;
import com.esd.model.data.persisted.InvoiceItem;
import com.esd.model.exceptions.InvalidIdValueException;
import org.joda.time.LocalDate;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to invoices
 */
public class InvoiceDao {

    private ConnectionManager connectionManager = ConnectionManager.getInstance();
    private static InvoiceDao instance;
    private static String INSERT_INVOICE = "insert into invoice "+
            "(invoicedate, invoicetime, invoicestatus, statuschangedate, employeeid, patientid, privatepatient, appointmentid)"+
            "values(?,?,?,?,?,?,?,?)";
    private static String UPDATE_INVOICE = "update invoice set "+
            "invoicedate=?, "+
            "invoicetime=?, "+
            "invoicestatus=?, "+
            "statuschangedate=?, "+
            "employeeid=?, "+
            "patientid=?, "+
            "privatepatient=?, "+
            "appointmentid=? "+
            "where id =?";

    private InvoiceDao() {}

    private PreparedStatement setInsertUpdateStatement(Invoice invoice, String statementString) throws SQLException {
        Connection con = connectionManager.getConnection();
        PreparedStatement statement = con.prepareStatement(statementString);
        statement.setDate(1, Date.valueOf(invoice.getInvoiceDate().toString()));
        statement.setTime(2, Time.valueOf(invoice.getInvoiceTime().toString()));
        statement.setString(3, invoice.getInvoiceStatus().toString());
        statement.setString(4, invoice.getInvoiceStatusChangeDate().toString());
        statement.setInt(5, invoice.getEmployeeId());
        statement.setInt(6, invoice.getPatientId());
        statement.setBoolean(7, invoice.isPrivatePatient());
        statement.setInt(8, invoice.getAppointmentId());
        return statement;
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

    private InvoiceItem extractInvoiceItemFromResultSet(ResultSet resultSet) throws SQLException {
        InvoiceItem invoiceItem =  new InvoiceItem();
        invoiceItem.setId(resultSet.getInt(DaoConsts.ID));
        invoiceItem.setInvoiceId(resultSet.getInt(DaoConsts.INVOICE_ID_FK));
        invoiceItem.setCost(resultSet.getDouble(DaoConsts.INVOICEITEM_COST));
        invoiceItem.setQuantity(resultSet.getInt(DaoConsts.INVOICEITEM_QUANTITY));
        invoiceItem.setDescription(resultSet.getString(DaoConsts.INVOICEITEM_DESCRIPTION));
        return invoiceItem;
    }

    public List<Invoice> getAllInvoicesWithStatus(LocalDate start, LocalDate end, Optional<InvoiceStatus> status, boolean loadItems) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE)
                .withRestriction(Restrictions.greaterThanInclusive(DaoConsts.INVOICE_DATE, start))
                .withRestriction(Restrictions.lessThanInclusive(DaoConsts.INVOICE_DATE, end));

        if(status.isPresent()){
            queryBuilder.withRestriction(Restrictions.equalsRestriction(DaoConsts.INVOICE_STATUS, status.get().name()));
        }
        return processResultSetForInvoices(loadItems, queryBuilder.createStatement());
    }

    public List<Invoice> getInvoiceWithStatusChangeToThisPeriod(LocalDate start, LocalDate end, InvoiceStatus status,boolean loadItems) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE)
                .withRestriction(Restrictions.greaterThanInclusive(DaoConsts.INVOICE_STATUS_CHANGE_DATE, start))
                .withRestriction(Restrictions.lessThanInclusive(DaoConsts.INVOICE_STATUS_CHANGE_DATE, end))
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.INVOICE_STATUS, status.toString()));

        return processResultSetForInvoices(loadItems, queryBuilder.createStatement());
    }

    public List<Invoice> getAllInvoicesWithStatus(InvoiceStatus status, boolean loadItems) throws SQLException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE)
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.INVOICE_STATUS, status.toString()));

        return processResultSetForInvoices(loadItems, queryBuilder.createStatement());
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

    public void updateInvoice(Invoice invoice) throws SQLException, InvalidIdValueException {
        if(invoice.getId()==0){
            throw new InvalidIdValueException("invoice id must be populated to update invoice");
        }
        PreparedStatement statement = setInsertUpdateStatement(invoice, UPDATE_INVOICE);
        statement.setInt(9, invoice.getId()); //key to id update
        statement.executeUpdate();
    }

    public void createInvoice(Invoice invoice) throws SQLException, InvalidIdValueException {
        if(invoice.getId()!=0){
            throw new InvalidIdValueException("invoice id cannot be populated to create invoice");
        }
        PreparedStatement statement = setInsertUpdateStatement(invoice, INSERT_INVOICE);
        statement.executeUpdate();
    }

    public Invoice getInvoiceById(int id) throws SQLException, InvalidIdValueException {
        SelectQueryBuilder queryBuilder = new SelectQueryBuilder(DaoConsts.TABLE_INVOICE);
        queryBuilder.withRestriction(Restrictions.equalsRestriction(DaoConsts.ID, id));
        PreparedStatement statement = queryBuilder.createStatement();
        ResultSet resultSet = statement.executeQuery();
        if(!resultSet.next()){
            throw new InvalidIdValueException("could not find invoice by id: "+ id);
        }
        return extractInvoiceFromResultSet(resultSet);
    }

    public synchronized static InvoiceDao getInstance(){
        if(instance == null){
            instance = new InvoiceDao();
        }
        return instance;
    }
}

