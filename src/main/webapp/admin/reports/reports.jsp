<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.esd.model.reportgen.SystemReports" %>
<%@ page import="com.esd.model.reportgen.ReportType" %>
<html>
<head>
    <title>Generate Report</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../../res/components/titlebar.jsp" %>
            <main>
            <form method="POST" action="${pageContext.request.contextPath}/admin/reports" class="input_form">
                <label for="report">Report:</label>
                <select id="report" name="report">
                <%
                    for (String key : SystemReports.availableReports.keySet()) {
                        out.print("<option value=\"" + key + "\">" + SystemReports.availableReports.get(key).getReportName() + "</option>");
                    }
                %>
                </select>
                <br/>
                Report parameters: <br/>
                <label for="report">Start Date:</label>
                <input id="startDate" type="date" name="startDate"/><br/>
                <label for="endDate">End Date:</label>
                <input id="endDate" type="date" name="endDate"/><br/>
                <label for="reportType">Report type:</label>
                <select id="reportType" name="reportType">
                <%
                    for (ReportType key : ReportType.values()) {
                        out.print("<option value=\"" + key + "\">" + key.name() + "</option>");
                    }
                %>
                </select> <br/>
                <input type="submit" value="Generate Report">
            </form>
            </main>
        </div>
    </div>
</body>
</html>
