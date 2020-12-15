<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.data.AppointmentOptions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointment</title>
</head>
<body>
<h2>Appointment View Page</h2>
<h3>Perform Appointment Actions below</h3>
<div>
    <div>
        <%try {request.getParameter("message");} catch(Exception e) { } %>
    </div>
    <form method="post" action="${pageContext.request.contextPath}/appointment">
        <table>
            <% try {
                Appointment appointment = (Appointment)request.getAttribute("appointment");
            %>
            <tr>
                <td>Appointment Id</td>
                <td><input type="text" name=<%=DaoConsts.ID%> value="<%out.print(appointment.getId());%>" size="10" /></td>
            </tr>
            <tr>
                <td>Appointment Date</td>
                <td><input type="date" name=<%=DaoConsts.APPOINTMENT_DATE%> value="<%out.print(appointment.getAppointmentDate());%>" size="10" /></td>
            </tr>
            <tr>
                <td>Appointment Slot</td>
                <td><input type="time" name=<%=DaoConsts.APPOINTMENT_TIME%> value="<%out.print(appointment.getAppointmentTime());%>" size="10" /></td>
            </tr>
            <tr>
                <td>Appointment Slots</td>
                <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(appointment.getSlots());%>" size="10" /></td>
            </tr>
            <tr>
                <td>Appointment Slots</td>
                <td><input type="text" name=<%=DaoConsts.APPOINTMENT_STATUS%> value="<%out.print(appointment.getStatus());%>" size="10" /></td>
            </tr>
            <tr>
                <td>Patient Id</td>
                <td><input type="text" name=<%=DaoConsts.PATIENT_ID%> value="<%out.print(appointment.getPatientId());%>" size="10" /></td>
            </tr>
            <% } catch (Exception e) {
            } %>
            <input name="option" type="radio" value=<%=AppointmentOptions.UPDATE%> />Update
            <input name="option" type="radio" value=<%=AppointmentOptions.CREATE%> />Create
            <input type="submit"/>
        </table>
    </form>
</div>
</body>
</html>
