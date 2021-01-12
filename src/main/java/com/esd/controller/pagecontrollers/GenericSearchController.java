package com.esd.controller.pagecontrollers;

import com.esd.model.data.persisted.UserDetails;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericSearchController extends HttpServlet {

    protected abstract void getResult(HttpServletRequest request, HttpServletResponse response) throws IOException;
    protected abstract void performSearch(HttpServletRequest request, HttpServletResponse response) throws IOException;

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
}
