<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.AppointmentPlaceHolder" %>
<%@ page import="com.esd.controller.pagecontrollers.appointments.AppointmentBookingController" %>

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
                <div>
                <%
                    List<AppointmentPlaceHolder> placeHolders = (List<AppointmentPlaceHolder>) request.getAttribute(AppointmentBookingController.ATTRIBUTE_AVAILABLE_APPOINTMENTS);

                    if(placeHolders.isEmpty()){%>
                        Sorry there are no available appointments for this day
                    <% }else{
                        for(AppointmentPlaceHolder app : placeHolders){ %>
                            <span style="border:1px solid black; margin-left: 10px">
                                <%=app.getAppointmentTime()%>
                            </span>
                       <% }
                   }%>
                </div>
            </form>
        </main>
    </div>
</div>♠
</body>
</html>