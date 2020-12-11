<%--
  Created by IntelliJ IDEA.
  User: Jordan
  Date: 05/12/2020
  Time: 10:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.esd.model.reportgen.SystemReports" %>
<%@ page import="com.esd.model.reportgen.ReportType" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>
<html>
<head>
    <title>Admin Reports</title>
</head>
<body>
    <jsp:include page="../includes/header.jsp" />
    this is the placeholder page for the admin reports
    <% out.print("<span style=\"color:red;\">This is some text</span>"); %>
    <form method="POST" action="reports">
        Report: <select name="report">
                <%
                    for(String key : SystemReports.availableReports.keySet()){
                        out.print("<option value=\"" + key +"\">" + SystemReports.availableReports.get(key).getReportName() +"</option>");
                    }
                %>
                </select> <br/>
        Report parameters: <br/>
        Start Date: <input type="date" name="startDate"/><br/>
        End Date: <input type="date" name="endDate"/><br/>
        report type: <select name="reportType">
        <%
            for(ReportType key : ReportType.values()){
                out.print("<option value=\"" + key +"\">" + key.name() +"</option>");
            }
        %>
    </select> <br/>
        <button type="submit">Generate report</button>
    </form><br/>
  </body>
</html>
