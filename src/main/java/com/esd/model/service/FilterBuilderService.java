package com.esd.model.service;

import com.esd.model.dao.ConnectionManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class FilterBuilderService {

    private final String FROM = "from ?";
    private final String WHERE = " where ? = ?";
    private final String EQUALS = " ? = ? ";
    private final String AND = " and ";
    private final String OR  = " and ";
    private final String OPEN = " ( ";
    private final String CLOSE = " ) ";

    // build though class operation
    private String SELECT = "select * ";
    private ArrayList<String> SearchTerms;

    private Object TargetModelClass;

    private FilterBuilderService(Object TargetModelClass) {

        //target class is the model class that will be returned and used for filtering
        this.TargetModelClass = TargetModelClass;
        SearchTerms.add(this.TargetModelClass.getClass().getSimpleName());
        // initialise the select
        this.SELECT += FROM;
    }

    private static <T> T CreateClass(Class<T> T) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        return T.getDeclaredConstructor().newInstance();
    }

    public void addWhere(String SearchField, String SearchMatch) {
        try {
            //validate field
            this.TargetModelClass.getClass().getField(SearchField);
            // to where to clause
            this.SELECT += WHERE;
            SearchTerms.add(SearchField);
            SearchTerms.add(SearchMatch);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void addEquals(String SearchField, String SearchMatch) {
        try {
            //validate field
            this.TargetModelClass.getClass().getField(SearchField);
            // to equals to clause
            this.SELECT += EQUALS;
            SearchTerms.add(SearchField);
            SearchTerms.add(SearchMatch);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void addAnd() {
        this.SELECT += AND;
    }

    public void addOr() {
        this.SELECT += OR;
    }

    public void addOpen(){
        SELECT += OPEN;
    }

    public void addClose(){
        SELECT += CLOSE;
    }

    public ArrayList<Object> getFilterResults(){
        ArrayList<Object> returnList = null;

        try{
            //get connection
            Connection con = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(SELECT);

            //loop through search list
            for(int i = 1; i < SearchTerms.size(); i++) {
                statement.setString(i, SearchTerms.get(i));
            }

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                //todo map rs to class object
            }

            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnList;
    }
}
