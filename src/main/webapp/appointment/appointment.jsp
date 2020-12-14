<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="java.util.ArrayList" %>
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
                <td>
                    <input type="text" name=<%=DaoConsts.ID%> size="10" />
                </td>
                <td>
                    <%=appointment.getId()%>
                </td>
            </tr>
            <tr>
                <td>Appointment Date</td>
                <td>
                    <input type="date" name=<%=DaoConsts.APPOINTMENT_DATE%> size="10" />
                </td>
                <td>
                    <%=appointment.getAppointmentDate()%>
                </td>
            </tr>
            <tr>
                <td>Appointment Slot</td>
                <td>
                    <input type="time" name=<%=DaoConsts.APPOINTMENT_TIME%> size="10" />
                </td>
                <td>
                    <%=appointment.getAppointmentTime()%>
                </td>
            </tr>
            <tr>
                <td>Appointment Slots</td>
                <td>
                    <input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> size="10" />
                </td>
                <td>
                    <%=appointment.getSlots()%>
                </td>
            </tr>
            <tr>
                <td>Appointment Slots</td>
                <td>
                    <input type="text" name=<%=DaoConsts.APPOINTMENT_STATUS%> size="10" />
                </td>
                <td>
                    <%=appointment.getStatus()%>
                </td>
            </tr>
            <tr>
                <td>Patient Id</td>
                <td>
                    <input type="text" name=<%=DaoConsts.PATIENT_ID%> size="10" />
                </td>
                <td>
                    <%=appointment.getPatientId()%>
                </td>
            </tr>
            <% } catch (Exception e) {
            } %>
            <input name="option" type="radio" value="0" />Update
            <input name="option" type="radio" value="1" />Create
            <input type="submit"/>Submit
        </table>
    </form>
</div>
</body>
</html>
