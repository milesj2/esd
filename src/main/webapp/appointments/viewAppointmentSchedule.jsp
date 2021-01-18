<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.data.UIAppointment" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                <div class="schedule">
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
                                <div class="appointment">
                                    <h2>Example</h2>
                                    <h3>09:10 - 09:20</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
<script>
    var appointments = [
<%

    ArrayList<UIAppointment> appointments = (ArrayList<UIAppointment>)request.getAttribute("appointments");
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
</script>
</body>
</html>
