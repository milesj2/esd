<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointments</title>
    <title>Template</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
                <h2>Appointments</h2>
                <form method="post" action="${pageContext.request.contextPath}/appointments/search">
                    <table class="search_table" >
                        <tr>
                            <th>From Date</th>
                            <th>To Date</th>
                            <th>Slots</th>
                            <th>Status</th>
                            <th>Id</th>
                            <th sort="lock"></th>
                        </tr>
                        <tr>
                            <td><input type="date" name="fromDate" size="10" /></td>
                            <td><input type="date" name="toDate" size="10" /></td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> size="10" /></td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_STATUS%> size="10" /></td>
                            <td><input type="text" name=<%=DaoConsts.ID%> size="10" /></td>
                            <td><input type="submit" value="Search" /></td>
                        </tr>
                    </table>
                    <h4>Search results:</h4>
                    <table class="search_table">
                        <tr>
                            <th>Appointment Date</th>
                            <th>Appointment Time</th>
                            <th>Appointment Slots</th>
                            <th>Appointment Status</th>
                            <th>Patient Id</th>
                            <th sort="lock">Actions</th>
                        </tr>
                        <% try {
                            ArrayList<Appointment> appointmentList = (ArrayList<Appointment>)request.getAttribute("table");
                            for(Appointment appointment:appointmentList){ %>
                        <tr>
                            <td><%=appointment.getAppointmentDate()%></td>
                            <td><%=appointment.getAppointmentTime()%></td>
                            <td><%=appointment.getSlots()%></td>
                            <td><%=appointment.getStatus()%></td>
                            <td><%=appointment.getPatientId()%></td>
                            <td><a href='viewAppointment?<%=DaoConsts.ID%>=<%=appointment.getId()%>'>Amend Appointment</a></td>
                        </tr>
                        <% }
                        } catch(Exception e){
                        } %>
                    </table>
                </form>
            </main>
        </div>
    </div>
    <script>
        var contextPath = "${pageContext.request.contextPath}"
        addFuncToTableControl();
    </script>
</body>
</body>
</html>
