package com.esd.controller.search;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Original Author: Trent meier
 * Use: the invoice search form controller returns matching results from the search form's
 * parameters and redirects to the invoice search page
 */

@WebServlet("/invoiceSearchForm")
public class InvoiceSearchFormController extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        // get users
        String str = "12";

        // todo search filter params
        // String searchTerm = request.getParameter("Type")
        request.setAttribute("table", str);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("search/invoiceSearch.jsp");
        requestDispatcher.forward(request, response);
    }
}
