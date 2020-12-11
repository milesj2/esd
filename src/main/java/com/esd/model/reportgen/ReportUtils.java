package com.esd.model.reportgen;

import java.util.HashMap;
import java.util.List;

public class ReportUtils {

    public static final String generateHTMLReportHeader(String reportName, List<String> details){
        String genString = "<div><img src=\"#\" alt=\"logo\"/><span>" + reportName + "</span><br/>";
        for(String detail : details){
            genString += "<span>" + detail  + "</span>";
        }
        genString += "</div>";

        return genString;
    }
}
