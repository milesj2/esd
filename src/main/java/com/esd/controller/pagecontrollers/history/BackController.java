package com.esd.controller.pagecontrollers.history;


import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.Navigation;
import com.esd.model.data.UserGroup;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/back")
@Authentication(userGroups = {UserGroup.ALL})
public class BackController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = Navigation.popHistory();

        System.out.println("URL " + url);
        System.out.println("nav: " + Navigation.history.toString());
        request.setAttribute("back", "true");
        response.sendRedirect(request.getContextPath() + url);

    }
}
