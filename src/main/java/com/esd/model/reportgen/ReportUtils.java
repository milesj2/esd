package com.esd.model.reportgen;

import java.util.HashMap;
import java.util.List;

public class ReportUtils {

    public static final String generateHTMLReportHeader(String reportName, List<String> details){
        String genString = "<div><div><img src=\"../../res/images/logo.png\" alt=\"logo\"/><span></div>" + reportName + "</span><br/>";
        for(String detail : details){
            genString += "<div>" + detail  + "</div>";
        }
        genString += "</div>";

        return genString;
    }
}
