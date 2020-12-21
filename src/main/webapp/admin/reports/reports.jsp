<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.esd.model.reportgen.SystemReports" %>
<%@ page import="com.esd.model.reportgen.ReportType" %>
<html>
<head>
    <title>Generate Report</title>
</head>
    <body>
        <form method="POST" action="${pageContext.request.contextPath}/admin/reports">
            Report: <select name="report">
            <%
                for (String key : SystemReports.availableReports.keySet()) {
                    out.print("<option value=\"" + key + "\">" + SystemReports.availableReports.get(key).getReportName() + "</option>");
                }
            %>
            </select> <br/>
                Report parameters: <br/>
                Start Date: <input type="date" name="startDate"/><br/>
                End Date: <input type="date" name="endDate"/><br/>
                report type: <select name="reportType">
                <%
                    for (ReportType key : ReportType.values()) {
                        out.print("<option value=\"" + key + "\">" + key.name() + "</option>");
                    }
                %>
            </select> <br/>
            <button type="submit">Generate report</button>
        </form>
    </body>
</html>
