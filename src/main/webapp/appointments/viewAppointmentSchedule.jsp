<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.data.UIAppointment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% ArrayList<UIAppointment> appointments = (ArrayList<UIAppointment>)request.getAttribute("appointments"); %>
<html>
<head>
    <title>Template</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
    <script src="../res/js/schedule.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
                <h3 id="week_beginning"></h3>
                <div id="schedule_switch">
                    <button onclick="viewDay();" >List View</button>
                    <button onclick="viewWeek();">Grid View</button>
                </div>
                <div id="week_schedule" class="week_schedule">
                    <div id="time_bar">
                        <div class="circle"></div>
                        <div class="line"></div>
                    </div>
                    <div id="day_headers" class="day_headers">
                    </div>
                    <div id="schedule_main" class="schedule_main">
                        <div id="times" class="times_container">
                        </div>
                        <div id=schedule_body" class="schedule_body">
                            <div class="appointments_day_container">
                            </div>
                            <div class="appointments_day_container">
                            </div>
                            <div class="appointments_day_container">
                            </div>
                            <div class="appointments_day_container">
                            </div>
                            <div class="appointments_day_container">
                            </div>
                            <div class="appointments_day_container">
                            </div>
                            <div class="appointments_day_container">
                            </div>
                        </div>
                    </div>
                </div> <!-- ./week_schedule -->
                <div id="day_schedule" class="day_schedule">
                </div> <!-- ./day_schedule -->
            </main>
        </div>
    </div>
<script>
    var appointments = [
<%
    if (appointments != null){
        for (UIAppointment appointment:appointments){
            out.print("{ ");
            out.print("id: '" + appointment.getId() +"',");
            out.print("title: '" + appointment.getTitle() + "',");
            out.print("date: '" + appointment.getAppointmentDate().toString() + "',");
            out.print("time: '" + appointment.getAppointmentTime().toString() + "',");
            out.print("slots: '" + appointment.getSlots() + "',");
            out.print("},\n");
        }
    }
%>
    ];
</script>
<script>
    initialise_schedule();
    initialise_day_schedule();
</script>
</body>
</html>
