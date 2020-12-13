package com.esd.model.dao;

import com.esd.model.data.InvoiceStatus;
import com.esd.model.data.persisted.Invoice;

import javax.servlet.http.HttpServletRequest;

import com.esd.model.data.persisted.InvoiceItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    private static String INVOICE_STATUS_RESTRICTION = "AND invoicestatus = ?";

    private static String GET_ALL_INVOICE_WITH_STATUS_RESTRICTION = "select * from invoice\n" +
            "    left outer join INVOICEITEM on INVOICEITEM.invoiceID = invoice.id\n" +
            "    where  invoicestatus = ?";

    private static String GET_COMPLETE_INVOICE_DATERESTRICTION = "select * from invoice\n" +
            "    left outer join INVOICEITEM on INVOICEITEM.invoiceID = invoice.id\n" +
            "    where INVOICEDATE >= ? AND INVOICEDATE <= ? ";

    private static String GET_INVOICE_STATUS_CHANGE_THIS_PERIOD = "select * from invoice\n" +
            "    left outer join INVOICEITEM on INVOICEITEM.invoiceID = invoice.id\n" +
            "    where statusChangeDate >= ? AND statusChangeDate <= ? AND invoicestatus = ?";

    private static String GET_INVOICE_DATE_RESTRICTION = "select * from invoice\n" +
            "    where INVOICEDATE >= ? AND INVOICEDATE <= ? ";

    private static String GET_INVOICE_ITEM_FOR_INVOICE = "select * from invoiceitem\n" +
            "    where invoiceitem.invoiceID = ? ";

    private InvoiceDao() {}

    public List<Invoice> getAllInvoicesAndItemsBetweenDatesAndWithStatus(Date start, Date end, Optional<InvoiceStatus> status, boolean loadItems) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        String query = loadItems ? GET_COMPLETE_INVOICE_DATERESTRICTION : GET_INVOICE_DATE_RESTRICTION;
        if(status.isPresent()){
            query += INVOICE_STATUS_RESTRICTION;
        }

        PreparedStatement statement = con.prepareStatement(query);
        statement.setDate(1, new java.sql.Date(start.getTime()));
        statement.setDate(2, new java.sql.Date(start.getTime()));

        if(status.isPresent()){
            statement.setString(3, status.get().name());
        }

        return processResultSetForInvoices(loadItems, statement);
    }

    public List<Invoice> getInvoiceWithStatusChangeToThisPeriod(Date start, Date end, InvoiceStatus status,boolean loadItems) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(GET_INVOICE_STATUS_CHANGE_THIS_PERIOD);
        statement.setDate(1, new java.sql.Date(start.getTime()));
        statement.setDate(2, new java.sql.Date(start.getTime()));
        statement.setString(3, status.name());
        return processResultSetForInvoices(loadItems, statement);
    }

    public List<Invoice> getAllInvoicesAndItemsBetweenDatesAndWithStatus(InvoiceStatus status, boolean loadItems) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        String query = GET_ALL_INVOICE_WITH_STATUS_RESTRICTION;


        PreparedStatement statement = con.prepareStatement(query);

        statement.setString(1, status.name());

        return processResultSetForInvoices(loadItems, statement);
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
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(GET_INVOICE_ITEM_FOR_INVOICE);
        statement.setInt(1, id);

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
        invoice.setEmployeeId(resultSet.getInt(DaoConsts.EMPLOYEE_ID));
        invoice.setPatientId(resultSet.getInt(DaoConsts.PATIENT_ID));
        invoice.setPrivatePatient(resultSet.getBoolean(DaoConsts.PRIVATE_PATIENT));
        invoice.setAppointmentId(resultSet.getInt(DaoConsts.APPOINTMENT_ID));
        return invoice;
    }


    private InvoiceItem extractInvoiceItemFromResultSet(ResultSet resultSet) throws SQLException {
        InvoiceItem invoiceItem =  new InvoiceItem();
        invoiceItem.setId(resultSet.getInt(DaoConsts.ID));
        invoiceItem.setInvoiceId(resultSet.getInt(DaoConsts.INVOICE_ID));
        invoiceItem.setCost(resultSet.getDouble(DaoConsts.INVOICE_ITEM_COST));
        invoiceItem.setQuantity(resultSet.getInt(DaoConsts.INVOICE_ITEM_QUANTITY));
        invoiceItem.setDescription(resultSet.getString(DaoConsts.INVOICE_ITEM_DESCRIPTION));
        return invoiceItem;
    }

    public ArrayList<Invoice> getFilteredDetails(ArrayList<String> formKey, HttpServletRequest request){

        ArrayList<Invoice> invoiceLst = new ArrayList<Invoice>();
        String STATEMENT_BUILDER = GET_FILTERED_INVOICES;

        try {
            boolean first = true;
            for(String key: formKey){
                if(!request.getParameter(key).isEmpty()) {
                    if(first){
                        STATEMENT_BUILDER += WHERE+key+MATCH;
                        first = false;
                    } else {
                        STATEMENT_BUILDER += AND+key+MATCH;
                    }
                }
            }

            //get connection
            Connection con = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(STATEMENT_BUILDER);

            int i=1;  //set statement values
            for(String key: formKey){
                if(!request.getParameter(key).isEmpty()) {
                    statement.setString(i,(String)request.getParameter(key));
                    i+=1;
                }
            }

            ResultSet result = statement.executeQuery();

            // add results to list of user to return
            while(result.next()){
                invoiceLst.add(extractInvoiceFromResultSet(result));
            }

            // close statement and result set
            statement.close();
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return invoiceLst;
    }

    public synchronized static InvoiceDao getInstance(){
        if(instance == null){
            instance = new InvoiceDao();
        }
        return instance;
    }
}

