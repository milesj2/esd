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
<%@ page import="com.esd.model.data.persisted.Prescription" %>

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
            <h1>Appointment in progress</h1>
            <form method="POST" action="${__SELF}">
                Notes:</br>
                <textarea name="notes" rows="20" cols="50"><%=request.getAttribute("notes")%></textarea></br>
                Patient Referal:
                <select name="referalId">
                    <option value="-1" <%=(Integer)request.getAttribute("referalId") == -1 ? "selected" : ""%>>None</option>

                    <% for(ThirdParty thirdParty : (List<ThirdParty>)request.getAttribute("thirdPartys")){ %>
                        <option value="<%=thirdParty.getId()%>" <%=(Integer)request.getAttribute("referalId") == thirdParty.getId() ? "selected" : ""%>><%=thirdParty.getName()%></option>
                    <% } %>
                </select></br>
                <% Prescription prescription = (Prescription) request.getAttribute("prescription");
                if(prescription == null){ %>
                    <input type="submit" name="issuePrescription" value="Issue Prescription"/>
                <% }else{%>
                    <input type="submit" name="editPrescription" value="Edit Prescription"/>
                <% } %>

                <input type="submit" name="completeAppointment" value="Complete Appointment"/>
            </form>
            <% if(prescription != null){ %>
            <a href="${pageContext.request.contextPath}/prescriptions/pdf?selectedPrescriptionId=<%=prescription.getId()%>" target="_blank">
                <button class="input_form_button">
                    Download Prescription
                </button>
            </a>
            <% } %>
        </main>
    </div>
</div>
</body>
</html>