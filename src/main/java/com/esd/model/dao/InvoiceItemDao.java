package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.persisted.InvoiceItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, The use of this class is to all DAO operations in relation to invoceItems
 */

public class InvoiceItemDao {
    
    private ConnectionManager connectionManager = ConnectionManager.getInstance();
    private static InvoiceItemDao instance;
    
    private static String INSERT_INVOICEITEM = "insert into invoiceitem" +
            " (invoiceid, itemcost, quantity, description) " +
            "values (?,?,?,?)";
    private static  String UPDATE_INVOICEITEM = "update invoiceitem set "+
            "invoiceid = ?, itemcost = ?, quantity = ?, description = ? where id=?";
    
    public InvoiceItemDao(){
        
    }

    private PreparedStatement InsertUpdateStatementInvoiceItem(InvoiceItem invoiceItem, String statementString) throws SQLException {
        Connection con = connectionManager.getConnection();
        PreparedStatement statement = con.prepareStatement(statementString);
        statement.setInt(1, invoiceItem.getInvoiceId());
        statement.setDouble(2,invoiceItem.getCost());
        statement.setInt(3, invoiceItem.getQuantity());
        statement.setString(4, invoiceItem.getDescription());
        return statement;
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

    public void createInvoiceItem(InvoiceItem invoiceItem) throws SQLException {
        PreparedStatement statement = InsertUpdateStatementInvoiceItem(invoiceItem, INSERT_INVOICEITEM);
        statement.executeUpdate();
    }

    public void updateInvoiceItem(InvoiceItem invoiceItem) throws SQLException {
        PreparedStatement statement = InsertUpdateStatementInvoiceItem(invoiceItem, UPDATE_INVOICEITEM);
        statement.setInt(5, invoiceItem.getId());
        statement.executeUpdate();
    }

    public synchronized static InvoiceItemDao getInstance(){
        if(instance == null){
            instance = new InvoiceItemDao();
        }
        return instance;
    }
}
