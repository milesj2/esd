package com.esd.model.dao.queryBuilders;

import com.esd.model.dao.ConnectionManager;
import com.esd.model.dao.queryBuilders.joins.Join;
import com.esd.model.dao.queryBuilders.restrictions.Restriction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.text.SimpleDateFormat;
import java.util.*;

public class SelectQueryBuilder{
    String table;
    String tableAlias;

    List<Restriction> restrictions = new ArrayList<>();
    List<Join> joins = new ArrayList<>();
    HashMap<String, String> columnAlias = new HashMap<>();

    public SelectQueryBuilder(String table, String tableAlias) {
        this.table = table;
        this.tableAlias = tableAlias;
    }

    public SelectQueryBuilder(String table) {
        this.table = table;
    }

    public SelectQueryBuilder withRestriction(Restriction restriction){
        restrictions.add(restriction);
        return this;
    }

    public SelectQueryBuilder and(Restriction restriction){
        restriction.setOperation("AND");
        restrictions.add(restriction);
        return this;
    }

    public SelectQueryBuilder or(Restriction restriction){
        restriction.setOperation("OR");
        restrictions.add(restriction);
        return this;
    }

    public SelectQueryBuilder withJoin(Join join){
        this.joins.add(join);
        return this;
    }

    public PreparedStatement createStatement() throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        List<Object> theMap = new ArrayList<>();
        String query = "Select ";
        if(!columnAlias.isEmpty()){
            boolean isFirst = true;
            for(String key : columnAlias.keySet()){
                if(!isFirst){
                    query += ", ";
                }
                query += key + " as " + columnAlias.get(key) + "";
                isFirst = false;
            }
        }else{
            query += " *";
        }
        query += " from " + table;
        if(tableAlias != null){
            query += " " + tableAlias;
        }
        for (Join join : joins) {
            query += " " + join.generateSQL();
        }

        boolean hasWhere = false;
        int currentCount = 1;

        for(Restriction restriction : restrictions){
            Object[] o = restriction.getValues();
            if(!hasWhere) {
                hasWhere = true;
                query += " where ";
            }else{
                query += " " + restriction.getOperator() + " ";
            }
            query +=  restriction.generateSql();
            if(o != null){
                theMap.addAll(Arrays.asList(o));
            }
            currentCount ++;
        }
        System.out.println(query);
        PreparedStatement statement = con.prepareStatement(query);

        for (int i = 1; i <= theMap.size(); i++) {
            Object x = theMap.get(i - 1);

            if (x instanceof Date){
                statement.setDate(i, new java.sql.Date(((Date) x).getTime()));
            }else{
                statement.setString(i, x.toString());
            }
        }
        return statement;
    }

    public SelectQueryBuilder selectColumn(String field, String alias) {
        columnAlias.put(field, alias);
        return this;
    }
    public SelectQueryBuilder selectColumn(String field) {
        columnAlias.put(field, field.replace(".", "_"));
        return this;
    }
}
