<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.AppointmentPlaceHolder" %>
<%@ page import="com.esd.controller.pagecontrollers.appointments.AppointmentBookingController" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.service.UserDetailsService" %>
<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>
<%@ page import="com.esd.model.service.SystemUserService" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>

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
            <% if(!(Boolean)request.getAttribute("confirmed")) {%>
                <h1>Please confirm appointment details</h1>
            <% }else{ %>
                <h1><%=request.getAttribute("confirmationMessage")%>></h1>
            <% } %>

        <% if(request.getAttribute("errorMessage") != null){ %>
            <span class="errMessage"><%= request.getAttribute("errorMessage") %></span>
        <% } %>

            <% if(request.getAttribute("originalAppointment") != null){
                Appointment originalAppointment = (Appointment) request.getAttribute("originalAppointment");
             UserDetails employeeDetails =
                    UserDetailsService.getInstance().getUserDetailsByID(originalAppointment.getEmployeeId());
                UserGroup group = SystemUserService.getInstance().getUserByUserDetailsId(employeeDetails.getId()).getUserGroup();

            %>
                <h1>Original Appointment details</h1>
            <% if(request.getParameter("selectedUserId") != null){
                UserDetails patient =
                        UserDetailsService.getInstance().getUserDetailsByID(Integer.parseInt(request.getParameter("selectedUserId")));
            %>
                Patient: <%= patient.getFirstName() + patient.getLastName()%></br>
            <% } %>

            With: <%= group==UserGroup.DOCTOR ? "Dr ":"Nurse "%> <%=employeeDetails.getLastName()%></br>
            Appointment Date: <%=originalAppointment.getAppointmentDate() %></br>
            Appointment Time: <%=originalAppointment.getAppointmentTime() %></br>
            Slots: <%=originalAppointment.getSlots() %></br>

            <% } %>
            <h1>Appointment details</h1>
            <form method="post" action="<%=UrlUtils.absoluteUrl(request, "appointments/confirm")%>">
                <input type="hidden" name="action" value="confirm"/>
                <input type="hidden" readonly name="employeeId" value="<%=request.getParameter("employeeId")%>"/>
                <% if(request.getParameter("selectedUserId") != null){
                    UserDetails patient =
                        UserDetailsService.getInstance().getUserDetailsByID(Integer.parseInt(request.getParameter("selectedUserId")));
                %>
                    <input type="hidden" readonly name="selectedUserId" value="<%=request.getParameter("selectedUserId")%>"/>

                    Patient: <%= patient.getFirstName() + patient.getLastName()%>
                <% } %>

                <% if(request.getParameter("selectedAppointmentId") != null){ %>
                <input type="hidden" readonly name="selectedAppointmentId" value="<%=request.getParameter("selectedAppointmentId")%>"/>
                <% } %>

                Appointment Date:
                <input type="date" readonly name="appointmentDate" value="<%=request.getParameter("appointmentDate")%>"/> </br>

                <% UserDetails employeeDetails =
                        UserDetailsService.getInstance().getUserDetailsByID(Integer.parseInt(request.getParameter("employeeId")));
                UserGroup group = SystemUserService.getInstance().getUserByUserDetailsId(employeeDetails.getId()).getUserGroup();
                %>

                With: <input type="text" readonly value="<%= group==UserGroup.DOCTOR?"dr ":"Nurse "%> <%=employeeDetails.getLastName()%>"/></br>
                Slots: <input type="text" readonly name="slots" value="<%=request.getParameter("slots")%>"/></br>
                Appointment Date: <input type="text" readonly name="appointmentDate" value="<%=request.getParameter("appointmentDate")%>"/></br>
                Time: <input type="text" readonly name="appointmentTime" value="<%=request.getParameter("appointmentTime")%>"/></br>

                <% if(!(Boolean)request.getAttribute("confirmed")) {%>
                    <input type="submit" value="Confirm Appointment"/>
                <% } %>

            </form>
        </main>
    </div>
</div>
</body>
</html>