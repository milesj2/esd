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
<h4> Search below</h4>
<div>
    <form method="post" action="${pageContext.request.contextPath}/appointments/schedule">
        <table border="1" cellpadding="5">
            <tr>
                <th>From Date</th>
                <th>To Date</th>
                <th>Slots</th>
                <th>Status</th>
                <th>Id</th>
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
        <h4>
            search results:
        </h4>
        <table border="1" cellpadding="5">
            <% try {
                ArrayList<Appointment> appointmentList = (ArrayList<Appointment>)request.getAttribute("table");
            out.print("<tr>");
                out.print("<th>Appointment Date</th> ");
                out.print("<th>Appointment Time</th>");
                out.print("<th>Appointment Slots</th>");
                out.print("<th>Appointment Status</th>");
                out.print("<th>Patient Id</th>");
                out.print("<th>Actions</th>");
            out.print("</tr>");
                for(Appointment appointment:appointmentList){ %>
            <tr>
                <td><%=appointment.getAppointmentDate()%></td>
                <td><%=appointment.getAppointmentTime()%></td>
                <td><%=appointment.getSlots()%></td>
                <td><%=appointment.getStatus()%></td>
                <td><%=appointment.getPatientId()%></td>
                <td><a href='appointment?<%=DaoConsts.ID%>=<%=appointment.getId()%>'>Amend Appointment</a></td>
            </tr>
            <% }
            } catch(Exception e){
            } %>
        </table>
    </form>
</div>
</body>
</html>
