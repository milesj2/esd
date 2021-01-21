package com.esd.controller.pagecontrollers;

import com.esd.controller.pagecontrollers.search.SearchColumn;
import com.esd.controller.pagecontrollers.search.searchrow.SearchRow;
import com.esd.controller.utils.AuthenticationUtils;
import com.esd.model.data.persisted.SystemUser;
import com.esd.model.service.UserDetailsService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericSearchController extends HttpServlet {

    protected List<SearchColumn> columns;
    protected String selectedKey;



    public abstract List<SearchRow> getSearchResults(SystemUser currentUser, Map<String, Object> args);


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.setAttribute("previousPage", session.getAttribute("currentPage"));
        session.setAttribute("currentPage", request.getServletPath());
        request.setAttribute("pageTitle", "Search");
        request.setAttribute("columns", columns);
        setDefaultColumValue(request);

        RequestDispatcher view = request.getRequestDispatcher("/search.jsp");
        view.forward(request, response);
    }

    protected void getResult(HttpServletRequest request, HttpServletResponse response) throws IOException {

        URIBuilder redirectURIBuilder = null;
        try {
            redirectURIBuilder = new URIBuilder(request.getParameter("redirect"));

            if(request.getParameter("selectedKey") != null) {
                redirectURIBuilder.addParameter(selectedKey, request.getParameter("selectedKey"));
            }

            response.sendRedirect(redirectURIBuilder.build().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    protected void performSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> args =  new HashMap<>();
        for(SearchColumn searchColumn: columns) {
            if(checkRequestContains(request, searchColumn.getField())){
                args.put(searchColumn.getField(), request.getParameter(searchColumn.getField()));
            }
        }
        try {
            // pass request with form keys and request (has post values)
            SystemUser user = AuthenticationUtils.getCurrentUser(request);
            if(user.getUserDetails() == null){
                user.setUserDetails(UserDetailsService.getInstance().getUserDetailsByUserID(user.getId()));
            }
            List<SearchRow> resultList = getSearchResults(user, args);

            request.setAttribute("table", resultList);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/search.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void setDefaultColumValue(HttpServletRequest request){
        for(SearchColumn column : columns){
            String value = request.getParameter(column.getField());
            String key = "val"+column.getField();
            if(value != null){
                request.setAttribute(key, value);
            }else{
                request.setAttribute(key, "");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String type = request.getParameter("type");
        request.setAttribute("columns", columns);
        setDefaultColumValue(request);
        
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
