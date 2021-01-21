<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.AppointmentPlaceHolder" %>
<%@ page import="com.esd.controller.pagecontrollers.appointments.AppointmentBookingController" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.service.UserDetailsService" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>
<%@ page import="org.joda.time.LocalDate" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% SystemUser currentUser = (SystemUser) (session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>Appointment booking</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <form action="${__SELF}">
                <input onchange="submit()" min="<%=new LocalDate()%>" name="<%= AppointmentBookingController.ATTRIBUTE_SELECTED_DATE %>" type="date" value="<%=request.getAttribute(AppointmentBookingController.ATTRIBUTE_SELECTED_DATE) %>">
                <% if(request.getParameter("selectedUserId") != null){ %>
                    <input type="hidden" name="selectedUserId" value="<%=request.getParameter("selectedUserId")%>">
                <% } %>

                <% if(request.getParameter("selectedAppointmentId") != null){ %>
                <input type="hidden" name="selectedAppointmentId" value="<%=request.getParameter("selectedAppointmentId")%>">
                <% } %>

            </form>
            <%
                Map<Integer, List<AppointmentPlaceHolder>> doctorAppointments =
                        (Map<Integer, List<AppointmentPlaceHolder>>) request.getAttribute(AppointmentBookingController.ATTRIBUTE_AVAILABLE_APPOINTMENTS_DOCTOR);

                Map<Integer, List<AppointmentPlaceHolder>> nurseAppointments =
                        (Map<Integer, List<AppointmentPlaceHolder>>) request.getAttribute(AppointmentBookingController.ATTRIBUTE_AVAILABLE_APPOINTMENTS_NURSE);
            %>

                <div>
                    <h1>Available Doctor Appointments</h1>
                    <% if(doctorAppointments.isEmpty()){%>
                        Sorry there are no available appointments for this day
                    <% }else{
                        for(Integer id : doctorAppointments.keySet()){
                    %>
                    <div>
                        <h2>Dr <%=UserDetailsService.getInstance().getUserDetailsByID(id).getLastName()%></h2>
                        <form method="post" action="<%=UrlUtils.absoluteUrl(request, "appointments/confirm")%>">
                            <input type="hidden" name="employeeId" value="<%=id%>"/>
                            <input type="hidden" name="patientId" value="<%=request.getAttribute("patientId")%>">
                            <input type="hidden" name="appointmentDate"
                                   value="<%=request.getAttribute(AppointmentBookingController.ATTRIBUTE_SELECTED_DATE)%>"/>
                            <input type="hidden" name="slots" value="1"/>
                            <input type="hidden" name="action" value="book"/>
                            <% if(request.getParameter("selectedUserId") != null){ %>
                            <input type="hidden" name="selectedUserId" value="<%=request.getParameter("selectedUserId")%>"/>
                            <% } %>

                            <% if(request.getParameter("selectedAppointmentId") != null){ %>
                                <input type="hidden" name="selectedAppointmentId" value="<%=request.getParameter("selectedAppointmentId")%>"/>
                            <% } %>

                            <% if( doctorAppointments.get(id).isEmpty()){ %>
                                Sorry there are no available appointments for this day
                                <% }else{
                                    for(AppointmentPlaceHolder placeHolder : doctorAppointments.get(id)){%>
                                    <input type="submit" name="appointmentTime" value="<%=placeHolder.getAppointmentTime()%>"/>
                                <% }
                            }%>
                        </form>

                    </div>
                    <% }
                    }%>
                </div>

            <div>
                <h1>Available Nurse Appointments</h1>
                <% if(nurseAppointments.isEmpty()){%>
                Sorry there are no available appointments for this day
                <% }else{

                    for(Integer id : nurseAppointments.keySet()){
                %>
                <div>
                    <h2>Nurse <%=UserDetailsService.getInstance().getUserDetailsByID(id).getLastName()%></h2>
                    <form method="post" action="<%=UrlUtils.absoluteUrl(request, "appointments/confirm")%>">
                        <input type="hidden" name="employeeId" value="<%=id%>"/>
                        <input type="hidden" name="appointmentDate"
                               value="<%=request.getAttribute(AppointmentBookingController.ATTRIBUTE_SELECTED_DATE)%>"/>
                        <input type="hidden" name="slots" value="1"/>
                        <input type="hidden" name="action" value="book"/>
                        <input type="hidden" name="patientId" value="<%=request.getAttribute("patientId")%>">
                        <% if(request.getParameter("selectedUserId") != null){ %>
                        <input type="hidden" name="selectedUserId" value="<%=request.getParameter("selectedUserId")%>"/>
                        <% } %>

                        <% if(request.getParameter("selectedAppointmentId") != null){ %>
                        <input type="hidden" name="selectedAppointmentId" value="<%=request.getParameter("selectedAppointmentId")%>"/>
                        <% } %>

                        <% if( nurseAppointments.get(id).isEmpty()){ %>
                        Sorry there are no available appointments for this day
                        <% }else{
                            for(AppointmentPlaceHolder placeHolder : nurseAppointments.get(id)){%>
                        <input type="submit" name="appointmentTime" value="<%=placeHolder.getAppointmentTime()%>"/>
                        <% }
                        }%>
                    </form>

                </div>
                <% }
                }%>
            </div>
        </main>
    </div>
</div>
</body>
</html>