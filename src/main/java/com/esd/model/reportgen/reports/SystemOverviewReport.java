package com.esd.model.reportgen.reports;

import com.esd.model.data.UserGroup;
import com.esd.model.reportgen.Report;
import com.esd.model.reportgen.ReportType;
import com.esd.model.reportgen.ReportUtils;
import com.esd.model.service.reports.SystemOverViewReportService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SystemOverviewReport implements Report {

    private static final String REPORT_NAME = "MAIN_REPORT";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    private static final String ERROR_DIV = "<div class=\"report\" style=\"color: red;\">All form data is needed</div>";
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

        Date startDate;
        Date endDate;
        try {
            
            startDate = dateFormat.parse(parameterMap.get("startDate")[0]);
            endDate = dateFormat.parse(parameterMap.get("endDate")[0]);
        }catch (Exception e){
            return ERROR_DIV;
        }

        Map<String, String> reportData = SystemOverViewReportService.getInstance().getReportData();

        String report = reportTemplate;
        report = report.replace("$HEADER",
                ReportUtils.generateHTMLReportHeader(getReportName(),
                        Arrays.asList("Date: 2020-01-01 TO 2020-01-31")));
        for(String key : reportData.keySet()){
            report = report.replace("$" + key, reportData.get(key));
        }

        return report;
    }
}
