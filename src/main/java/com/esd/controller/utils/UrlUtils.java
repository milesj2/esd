package com.esd.controller.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public static String error(HttpServletRequest request, int errorCode) {
        return UrlUtils.absoluteUrl(request, "error?errorCode=" + errorCode);
    }
}
