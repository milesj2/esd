<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.esd.model.reportgen.SystemReports" %>
<%@ page import="com.esd.model.reportgen.ReportType" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
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
                <input id="startDate" type="date" name="startDate" required/><br/>
                <label for="endDate">End Date:</label>
                <input id="endDate" type="date" name="endDate" required/><br/>
                <label for="reportType">Report type:</label>
                <select id="reportType" name="reportType">
                <%
                    for (ReportType key : ReportType.values()) {
                        out.print("<option value=\"" + key + "\">" + key.name() + "</option>");
                    }
                %>
                </select> <br/>
                <input id="GenerateReport" type="submit" value="Generate Report">
            </form>
                <a hidden id="DownloadButton" href="" target="_blank">
                    <button class="input_form_button">Download PDF </button>
                </a>
            </main>
        </div>
        <input id="baseURL" hidden value="${pageContext.request.contextPath}" />
    </div>
</body>
<script>
    document.getElementById("reportType").onchange = function(){

        let startdate = document.getElementById("startDate").value;
        let enddate = document.getElementById("endDate").value;
        let selection = document.getElementById("reportType").value;

        if(selection == "PDF"){
            document.getElementById("GenerateReport").style.display = "none";
            document.getElementById("DownloadButton").style.display = "block";
            let urlWithParams = document.getElementById("baseURL").value+ "/admin/reports/pdf?startDate="+startdate+"&endDate="+enddate;
            console.log(urlWithParams);
            document.getElementById("DownloadButton").href = urlWithParams;
        } else {
            document.getElementById("DownloadButton").style.display = "none";
            document.getElementById("GenerateReport").style.display = "block";
        }
    }
</script>
</html>
