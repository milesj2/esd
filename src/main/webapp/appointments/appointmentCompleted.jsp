<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.AppointmentPlaceHolder" %>
<%@ page import="com.esd.controller.pagecontrollers.appointments.AppointmentBookingController" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.service.UserDetailsService" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>
<%@ page import="com.esd.model.data.persisted.ThirdParty" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% SystemUser currentUser = (SystemUser) (session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>Appointment booking</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <h1>Appointment Completed</h1>
            <a href="<%=request.getAttribute("scheduleLink")%>">Back to schedule</a>
            <a href="<%=request.getAttribute("invoiceDownloadLink")%>">Download Invoice</a>
            <a href="<%=request.getAttribute("prescriptionViewLink")%>">View Prescriptions</a>
        </main>
    </div>
</div>
</body>
</html>