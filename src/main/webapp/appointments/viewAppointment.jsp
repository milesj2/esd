<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>
<%@ page import="com.esd.model.service.UserDetailsService" %>
<%@ page import="com.esd.model.data.AppointmentStatus" %>
<%@ page import="com.esd.controller.utils.AuthenticationUtils" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointment</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
                <h2>View & Amend Appointment Details</h2>
                <form class="input_form" method="post" action="${pageContext.request.contextPath}/appointments/viewAppointment">
                    <table>
                        <% Appointment appointment = (Appointment) request.getAttribute("appointment"); %>
                        <% try {

                            UserDetails employeeDetails = UserDetailsService.getInstance().getUserDetailsByUserID(appointment.getEmployeeId());
                            UserDetails patientDetails = UserDetailsService.getInstance().getUserDetailsByUserID(appointment.getPatientId());
                        %>
                        <tr>
                            <td><strong>Appointment ID:</strong></td>
                            <td><input name=<%=DaoConsts.ID%> value=<%=appointment.getId()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Patient ID:</strong></td>
                            <td><input name=<%=DaoConsts.PATIENT_ID%> value=<%=patientDetails.getUserId()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Patient first name:</strong></td>
                            <td><input  value=<%=patientDetails.getFirstName()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Patient last name:</strong></td>
                            <td><input  value=<%=patientDetails.getLastName()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Employee ID:</strong></td>
                            <td><input name=<%=DaoConsts.EMPLOYEE_ID%> value=<%=employeeDetails.getUserId()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Employee first name:</strong></td>
                            <td><input value=<%=employeeDetails.getFirstName()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Employee last name:</strong></td>
                            <td><input value=<%=employeeDetails.getLastName()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Appointment status:</strong></td>
                            <td><input name=<%=DaoConsts.APPOINTMENT_STATUS%> value=<%=appointment.getStatus()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Appointment date:</strong></td>
                            <td><input name=<%=DaoConsts.APPOINTMENT_DATE%> value=<%=appointment.getAppointmentDate()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Appointment time:</strong></td>
                            <td><input name=<%=DaoConsts.APPOINTMENT_TIME%> value=<%=appointment.getAppointmentTime()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Appointment slots:</strong></td>
                            <td><input name=<%=DaoConsts.APPOINTMENT_SLOTS%> value=<%=appointment.getSlots()%> readonly="readonly"/></td>
                        </tr>
                        <tr>
                            <td><strong>Is referred:</strong></td>
                            <%
                                String hasThirdPartyId = appointment.getThirdPartyId() > 0 ? "YES" : "NO";
                                out.print("<td><input value=\"" + hasThirdPartyId + "\" + readonly=\"readonly\"/></td>");
                            %>
                        </tr>
                        <% } catch (Exception e) {
                            e.printStackTrace();
                        } %>
                    </table>
                    <% if(appointment.getStatus() != AppointmentStatus.COMPLETE && appointment.getStatus() != AppointmentStatus.CANCELED){ %>
                    <div>
                        <% if(UserGroup.practitioner.contains(AuthenticationUtils.getCurrentUserGroup(request))) { %>
                            <input type="submit" name="inprogress" value="Start Appointment">
                        <% } %>
                        <input type="submit" name="amend" value="Amend">
                        <input type="submit" name="cancel" value="Cancel Appointment">
                        <% if (request.getAttribute("msg") != null) { %>
                        <br>
                        <strong><%=request.getAttribute("msg")%></strong>
                        <% } %>
                    </div>
                    <% } %>
                </form>
            </main>
        </div>
    </div>
</body>
</html>
