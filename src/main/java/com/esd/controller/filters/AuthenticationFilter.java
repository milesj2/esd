package com.esd.controller.filters;

import com.esd.controller.annotations.Authentication;
import com.esd.controller.utils.UrlUtils;
import com.esd.model.data.UserGroup;
import com.esd.model.data.persisted.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.MappingMatch;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //this technically should never happen. if it does we'll log it and forward the user
        if(! (servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)){
            System.err.println("Failed to process");
            return;
        }

        //use the casted objects from this point
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        User user = (User)request.getSession().getAttribute("currentSessionUser");

        HttpServletMapping mapping = request.getHttpServletMapping();
        MappingMatch mappingMatch = mapping.getMappingMatch();

        //this should always be true after we've filtered
        if(mappingMatch == MappingMatch.EXACT){
            try {
                Class<?> clazz = Class.forName(mapping.getServletName());
                Authentication annotation = clazz.getAnnotation(Authentication.class);

                //class missing annotation, possible security issue
                if(annotation == null){
                    System.out.println("WARNING: Servlet is missing authentication annotation and is not secure: " + mapping.getServletName());
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }

                //no authentication required
                if(!annotation.authenticationRequired()){

                    // having a user group on annotation, but marked as no authentication required,
                    // points to possible mistake in code base
                    if(annotation.userGroups().length == 0){
                        if(user != null && !annotation.loggedInUserAccess()){
                            response.sendRedirect(UrlUtils.absoluteUrl(request, "dashboard"));
                            return;
                        }
                        filterChain.doFilter(servletRequest,servletResponse);
                        return;
                    }
                    System.err.println("ERROR: Authenitcation is marked as not required but usergroups are marked to be restricted: " + mapping.getServletName());
                    response.sendRedirect(UrlUtils.error(request, HttpServletResponse.SC_UNAUTHORIZED));
                    return;
                }

                //authentication required

                //validate session
                if(user == null){
                    ((HttpServletResponse)servletResponse).sendRedirect(UrlUtils.absoluteUrl(request, "login"));
                    return;
                }

                //validate user group
                List<UserGroup> userGroupList = Arrays.asList(annotation.userGroups());

                if(!userGroupList.contains(UserGroup.ALL) && !userGroupList.contains(user.getUserGroup())){
                    response.sendRedirect(UrlUtils.error(request, HttpServletResponse.SC_UNAUTHORIZED));
                    return;
                }

                filterChain.doFilter(servletRequest,servletResponse);
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return;
        }

        //We don't care about default mapping so we can just forward the reqyest
        System.err.println("No mapping match handled for: " + mappingMatch.name());
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
