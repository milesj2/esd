package com.esd.model.reportgen.reports;

import com.esd.model.reportgen.Report;
import com.esd.model.reportgen.ReportType;
import com.esd.model.reportgen.ReportUtils;
import com.esd.model.service.reports.SystemOverViewReportService;

import org.joda.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class SystemOverviewReport implements Report {

    private static final String REPORT_NAME = "MAIN_REPORT";

    private static final String ERROR_DIV = "<div class=\"report\" style=\"color: red;\">Failed to process report, check input data, if problmen persists contact admin</div>";
    private static String reportTemplate;

    static{
        try {
            Files.readAllLines(Paths.get(SystemOverviewReport.class.getResource("/reportTemplates/html/overAllSystemReportTemplate.html").toURI())).forEach(line -> reportTemplate += line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReportType[] getSupportedReportTypes() {
        return new ReportType[0];
    }

    @Override
    public String getReportName() {
        return REPORT_NAME;
    }

    @Override
    public String generateReport(Map<String, String[]> parameterMap) {

        LocalDate startDate;
        LocalDate endDate;
        try {
            
            startDate = LocalDate.parse(parameterMap.get("startDate")[0]);
            endDate = LocalDate.parse(parameterMap.get("endDate")[0]);
        }catch (Exception e){
            return ERROR_DIV;
        }

        Map<String, String> reportData = null;
        try {
            reportData = SystemOverViewReportService.getInstance().getReportData(startDate, endDate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return ERROR_DIV;
        }

        String report = reportTemplate;
        String dateHeader = String.format("Date: %s TO %s", startDate, endDate);


        report = report.replace("$HEADER",
                ReportUtils.generateHTMLReportHeader(getReportName(),
                        Arrays.asList(dateHeader)));
        for(String key : reportData.keySet()){
            report = report.replace("$" + key, reportData.get(key));
        }

        return report;
    }
}
