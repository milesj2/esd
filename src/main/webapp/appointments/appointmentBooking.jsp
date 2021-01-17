<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.AppointmentPlaceHolder" %>
<%@ page import="com.esd.controller.pagecontrollers.appointments.AppointmentBookingController" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User currentUser = (User) (session.getAttribute("currentSessionUser"));%>
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
            <form>
                <input type="date" value="<%=request.getAttribute(AppointmentBookingController.ATTRIBUTE_SELECTED_DATE) %>">
            </form>
            <%
                Map<Integer, List<AppointmentPlaceHolder>> doctorAppointments =
                        (Map<Integer, List<AppointmentPlaceHolder>>) request.getAttribute(AppointmentBookingController.ATTRIBUTE_AVAILABLE_APPOINTMENTS_DOCTOR);

                Map<Integer, List<AppointmentPlaceHolder>> nurseAppointments =
                        (Map<Integer, List<AppointmentPlaceHolder>>) request.getAttribute(AppointmentBookingController.ATTRIBUTE_AVAILABLE_APPOINTMENTS_DOCTOR);
            %>

                <div>
                    <h1>Available Doctor Appointments</h1>
                    <% if(doctorAppointments.isEmpty()){%>
                    Sorry there are no available appointments for this day
                    <% }else{
                        for(Integer id : doctorAppointments.keySet()){
                    %>
                    <div>
                        <h2>Dr _____</h2>
                        <% for(AppointmentPlaceHolder placeHolder : doctorAppointments.get(id)){%>
                            <span style="border:1px solid black; margin-left: 10px">
                                <%=placeHolder.getAppointmentTime()%>
                            </span>
                        <% }%>
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
                    <h2>Dr _____</h2>
                    <% for(AppointmentPlaceHolder placeHolder : nurseAppointments.get(id)){%>
                    <span style="border:1px solid black; margin-left: 10px">
                                <%=placeHolder.getAppointmentTime()%>
                            </span>
                    <% }%>
                </div>
                <% }
                }%>
            </div>
        </main>
    </div>
</div>♠
</body>
</html>