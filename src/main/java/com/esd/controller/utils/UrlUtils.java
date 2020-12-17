package com.esd.controller.utils;

import javax.servlet.http.HttpServletRequest;

public class UrlUtils {

    public static String absoluteUrl(HttpServletRequest request, String resource){
        String contextPath = request.getContextPath();
        String url = "";
        if(contextPath != null && contextPath != ""){
            url = contextPath;
        }

        if(resource.startsWith("/")){
            url += resource;
        }else{
            url += "/" + resource;
        }
        return url;
    }
}
