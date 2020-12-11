package com.esd.model.reportgen;

import java.util.Map;

public interface Report {

    ReportType[] getSupportedReportTypes();
    String getReportName();
    public String generateReport(Map<String, String[]> parameterMap);
}
