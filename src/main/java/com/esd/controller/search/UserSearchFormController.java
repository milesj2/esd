package com.esd.controller.search;

import com.esd.model.dao.ConnectionManager;
import com.esd.model.dao.DaoConsts;
import com.esd.model.data.persisted.UserDetails;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Original Author: Trent meier
 * Use: the user search form controller returns matching results from the search form's
 * parameters and redirects to the user search page
 */

@WebServlet("/userSearchForm")
public class UserSearchFormController extends HttpServlet {

    private ArrayList<String> formValues =  new ArrayList<String>(Arrays.asList(
            DaoConsts.USERDETAILS_ID,
            DaoConsts.USERDETAILS_FIRSTNAME,
            DaoConsts.USERDETAILS_LASTNAME ,
            DaoConsts.USERDETAILS_ADDRESS1,
            DaoConsts.USERDETAILS_TOWN,
            DaoConsts.USERDETAILS_POSTCODE,
            DaoConsts.USERDETAILS_DOB));

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        ArrayList<UserDetails> userDetailsList = new ArrayList<UserDetails>();
        boolean where = true;
        String STATEMENT_BUILDER = "SELECT * FROM ADMIN.USERDETAILS ";

        try {
            // build query from form data
            for(String Value: formValues){
                if(!request.getParameter(Value).isEmpty()) {
                    if(where){
                        STATEMENT_BUILDER += "WHERE " + Value + " = ?";
                        where = false;
                    } else {
                        STATEMENT_BUILDER += " AND " + Value + " = ?";
                    }
                }
            }

            //get connection
            Connection con = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(STATEMENT_BUILDER);

            //set statement values
            int i=1;
            for(String Value: formValues){
                if(!request.getParameter(Value).isEmpty()) {
                    String val = request.getParameter(Value);
                    statement.setString(i,val);
                    i+=1;
                }
            }

            ResultSet result = statement.executeQuery();

            // add results to list of user to return
            while(result.next()){
                UserDetails userDetails =  new UserDetails(
                        result.getInt(DaoConsts.USERDETAILS_ID),
                        result.getInt(DaoConsts.SYSTEMUSER_ID),
                        result.getString(DaoConsts.USERDETAILS_FIRSTNAME),
                        result.getString(DaoConsts.USERDETAILS_LASTNAME),
                        result.getString(DaoConsts.USERDETAILS_ADDRESS1),
                        result.getString(DaoConsts.USERDETAILS_ADDRESS2),
                        result.getString(DaoConsts.USERDETAILS_ADDRESS3),
                        result.getString(DaoConsts.USERDETAILS_TOWN),
                        result.getString(DaoConsts.USERDETAILS_POSTCODE),
                        result.getString(DaoConsts.USERDETAILS_DOB)
                );
                userDetailsList.add(userDetails);
            }

            statement.close();
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //return user details list
        request.setAttribute("table", userDetailsList);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("search/userSearch.jsp");
        requestDispatcher.forward(request, response);
    }
}
