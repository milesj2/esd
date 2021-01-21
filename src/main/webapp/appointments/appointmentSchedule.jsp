<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="com.esd.controller.pagecontrollers.appointments.AppointmentScheduleController" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.controller.utils.AuthenticationUtils" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.*" %>
<%@ page import="org.joda.time.LocalDate" %>
<%@ page import="com.esd.model.service.SystemUserService" %>
<%@ page import="java.sql.Time" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>
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
            <% SystemUser currentUser = AuthenticationUtils.getCurrentUser(request); %>
            <form class="input_form" method="POST" action="${__SELF}">
        <% if (request.getAttribute(AppointmentScheduleController.ATTRIBUTE_SELECTED_USER) != null) { %>
                <input type="hidden" name="<%=AppointmentScheduleController.ATTRIBUTE_SELECTED_USER%>"
                       value="<%=request.getAttribute(AppointmentScheduleController.ATTRIBUTE_SELECTED_USER)%>"/>
        <% } %>
                <table>
                    <tr>
                        <th>Appointment Date</th>
                        <th>Day Span</th>
                        <% if (UserGroup.employees.contains(currentUser.getUserGroup())) { %>
                            <th>Search and Select User</th>
                            <th>Entire Schedule</th>
                        <% } %>
                    </tr>
                    <tr>
                        <td>
                            <input onchange="submit()" name="<%= AppointmentScheduleController.ATTRIBUTE_SELECTED_DATE %>"
                                   type="date"
                                   value="<%=request.getAttribute(AppointmentScheduleController.ATTRIBUTE_SELECTED_DATE) %>"/>
                        </td>
                        <td>
                            <input onchange="submit()" name="<%= AppointmentScheduleController.ATTRIBUTE_SELECTED_SPAN %>"
                                   type="number"
                                   value="<%=request.getAttribute(AppointmentScheduleController.ATTRIBUTE_SELECTED_SPAN) %>"/>
                        </td>

                <% if (UserGroup.employees.contains(currentUser.getUserGroup())) { %>
                        <td>
                        <input type="submit" name="<%=AppointmentScheduleController.ATTRIBUTE_SELECT_USER%>"
                               value="Select User"/>
                        </td>
                        <td>
                        <input onchange="submit()" type="checkbox"
                               name="<%=AppointmentScheduleController.ATTRIBUTE_ENTIRE_SCHEDULE%>"
                                <%=(Boolean)(request.getAttribute(AppointmentScheduleController.ATTRIBUTE_ENTIRE_SCHEDULE)) != null ? "checked" : ""%>/>

                        </td>
                <% } %>
                    </tr>
                </table>
            </form>
            <h2>Appointments</h2>
            <%
                HashMap<LocalDate, List<Appointment>> appointmentsMap = (HashMap<LocalDate, List<Appointment>>) request.getAttribute(AppointmentScheduleController.ATTRIBUTE_APPOINTMENT_MAP);

                if (appointmentsMap.isEmpty()) { %>
            <div>No appointments are booked for selected params</div>
            <% } else {
                List<LocalDate> dates = new ArrayList<LocalDate>(appointmentsMap.keySet());
                Collections.sort(dates);

                for (LocalDate date : dates) {
                    List<Appointment> appointments = appointmentsMap.get(date); %>
                    <div class="dateContainer">
                        <div class="dateColumn">
                            <%=date%>
                        </div>
                        <div class="appointmentRows">
                            <div class="spacer">
                            </div>
                            <% for (Appointment appointment : appointments) { %>
                                <div class="appointmentRow">
                                    <span class="appointmentTime"><%=new Time(appointment.getAppointmentTime().toDateTimeToday().getMillis())%>-<%=new Time(appointment.getEndTime().toDateTimeToday().getMillis())%></span>
                                    <span>reason: <%=appointment.getAppointmentReason().isEmpty() ? "No reason given" : appointment.getAppointmentReason()%></span>
                                    <span class="patientName">Patient Name: <%=appointment.getPatientDetails().getFullName()%></span>
                                    <%
                                        SystemUser employeeUser = SystemUserService.getInstance().getUserByUserDetailsId(appointment.getEmployeeDetails().getId());
                                    %>
                                    <span class="patientName">Practitioner: <%=employeeUser.getUserGroup() == UserGroup.DOCTOR ? "Dr " : "Nurse "%> <%=appointment.getEmployeeDetails().getLastName()%></span>
                                    <div class="appointmentLinks">
                                        <a href="<%=UrlUtils.absoluteUrl(request, "appointments/viewAppointment?selectedAppointmentId=" + appointment.getId())%>">View Appointment</a></div>
                                </div>
                            <% } %>
                        </div>
                    </div>

                <% }
            }%>
        </main>
    </div>
</div>
</body>
</body>
</html>
