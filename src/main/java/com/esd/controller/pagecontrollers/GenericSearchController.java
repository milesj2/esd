package com.esd.controller.pagecontrollers;

import org.apache.http.client.utils.URIBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class GenericSearchController extends HttpServlet {

    protected ArrayList<String> formValues;
    protected Function<Map<String, Object>, List<?>> searchFilterFunction;
    protected String selectedKey;
    protected String searchPage;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());
        RequestDispatcher view = request.getRequestDispatcher(searchPage);
        view.forward(request, response);
    }

    protected void getResult(HttpServletRequest request, HttpServletResponse response) throws IOException {

        URIBuilder redirectURIBuilder = null;
        try {
            redirectURIBuilder = new URIBuilder(request.getParameter("redirect"));

            if(request.getParameter(selectedKey) != null) {
                redirectURIBuilder.addParameter(selectedKey, request.getParameter(selectedKey));
            }

            response.sendRedirect(redirectURIBuilder.build().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    protected void performSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> args =  new HashMap<>();
        for(String key: formValues) {
            if(checkRequestContains(request, key)){
                args.put(key, request.getParameter(key));
            }
        }
        try {
            // pass request with form keys and request (has post values)
            List<?> resultList = searchFilterFunction.apply(args);
            request.setAttribute("table", resultList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(searchPage);
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String type = request.getParameter("type");

        switch (type){
            case "search":
                //the submitted button is the only button included in the request
                if(request.getParameter("search") != null){
                    performSearch(request, response);
                }else if(request.getParameter("cancel") !=null){
                    getResult(request, response);
                }
                break;
            case "result":
                getResult(request, response);
                break;
            default:
                System.err.println("Unhandled type for user search controller: " + type);
                break;
        }
    }

    private boolean checkRequestContains(HttpServletRequest request, String key){
        if(request.getParameterMap().containsKey(key) &&
                !request.getParameter(key).isEmpty() &&
                request.getParameter(key) != ""){
            return true;
        }
        return false;
    }
}
