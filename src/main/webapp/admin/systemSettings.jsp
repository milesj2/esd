<%@ page import="com.esd.model.service.SystemSettingService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage System Settings</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
        <form class="input_form" method="post" action="${pageContext.request.contextPath}/admin/settings">
            <h4>Consultation fees (£/h) for doctors and nurses</h4>
            <label>Doctor fee (default £150.00/h): </label>
            <input type="text" placeholder="Enter fee (£/h)" name=<%=SystemSettingService.SYSTEMSETTING_FEE_DOCTOR%> id=<%=SystemSettingService.SYSTEMSETTING_FEE_DOCTOR%> value="<%=request.getAttribute(SystemSettingService.SYSTEMSETTING_FEE_DOCTOR)%>">
            <br>

            <label>Nurse fee (default £100.00/h): </label>
            <input type="text" placeholder="Enter fee (£/h)" name=<%=SystemSettingService.SYSTEMSETTING_FEE_NURSE%> id=<%=SystemSettingService.SYSTEMSETTING_FEE_NURSE%> value ="<%=request.getAttribute(SystemSettingService.SYSTEMSETTING_FEE_NURSE)%>">
            <br>

            <h4>Consultation slot time (default 10 minutes)</h4>
            <label>Consultation slot time (minutes): </label>
            <input type="text" placeholder="Enter slot time (minutes)" name=<%=SystemSettingService.SYSTEMSETTING_SLOT_TIME%> id=<%=SystemSettingService.SYSTEMSETTING_SLOT_TIME%> value="<%=request.getAttribute(SystemSettingService.SYSTEMSETTING_SLOT_TIME)%>">
            <br>

            <button value="update">Update</button>
            <%if(request.getAttribute("notification") != null){%>
                <div><strong><%=request.getAttribute("notification")%></strong></div>
            <% } %>
        </form>
        </main>
    </div>
</div>
</body>
</html>
