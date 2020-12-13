<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointments</title>
</head>
<body>
<h2>Appointment page</h2>
<h3>Search invoces below</h3>
<div>
    <form method="post" action="${pageContext.request.contextPath}/appointments">
        <table border="1" cellpadding="5">
            <tr>
                <th>Id</th>
                <th>Appointment Date</th>
                <th>Appointment Time</th>
                <th>Appointment Slots</th>
                <th>Appointment Status</th>
                <th>Patient Id</th>
                <th>Actions</th>
            </tr>
            <tr>
                <td><input type="date" name=<%=DaoConsts.APPOINTMENT_FROMDATE%> size="10" /></td>
                <td><input type="date" name=<%=DaoConsts.APPOINTMENT_TODATE%> size="10" /></td>
                <td><input type="text" name=<%=DaoConsts.APPOINTMENT_STATUS%> size="10" /></td>
                <td>
                </td>
            </tr>
            <tr>
                <input type="submit" value="Search" />
            </tr>
            <% try {
                ArrayList<Appointment> appointmentList = (ArrayList<Appointment>)request.getAttribute("table");
                for(Appointment appointment:appointmentList){ %>
            <tr>
                <td><%=appointment.getId()%></td>
                <td><%=appointment.getAppointmentDate()%></td>
                <td><%=appointment.getAppointmentTime()%></td>
                <td><%=appointment.getSlots()%></td>
                <td><%=appointment.getStatus()%></td>
                <td><%=appointment.getPatientId()%></td>
                <td><a href='appointment?<%=DaoConsts.APPOINTMENT_ID%>=<%=appointment.getId()%>'>Search Invoice</a></td>
            </tr>
            <% }
            } catch(Exception e){
            } %>
        </table>
    </form>
</div>
</body>
</html>