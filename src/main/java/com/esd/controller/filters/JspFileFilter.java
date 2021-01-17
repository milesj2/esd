package com.esd.controller.filters;

import com.esd.controller.utils.UrlUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.MappingMatch;
import java.io.IOException;

@WebFilter(filterName = "JSPFilter")
public class JspFileFilter implements Filter {
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

        HttpServletMapping mapping = request.getHttpServletMapping();
        MappingMatch mappingMatch = mapping.getMappingMatch();

        //get rid of any kind of direct access to files, all files should be provided via a controller.
        if(mappingMatch == MappingMatch.EXTENSION){
            response.sendRedirect(UrlUtils.error(request, HttpServletResponse.SC_UNAUTHORIZED));
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
