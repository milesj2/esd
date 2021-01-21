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
            <% if(request.getAttribute("scheduleLink") != null){%>
            <a href="<%=request.getAttribute("scheduleLink")%>">
                <button class="input_form_button">Back to schedule</button>
            </a>
            <%}%>
            <% if(request.getAttribute("invoiceDownloadLink") != null){%>
            <a href="<%=request.getAttribute("invoiceDownloadLink")%>" target="_blank">
                <button class="input_form_button">Download Invoice</button>
            </a>
            <%}%>
            <% if(request.getAttribute("prescriptionViewLink") != null){%>
            <a href="<%=request.getAttribute("prescriptionViewLink")%>">
                <button class="input_form_button">View Prescriptions</button>
            </a>
            <%}%>
        </main>
    </div>
</div>
</body>
</html>