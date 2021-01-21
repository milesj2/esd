<%@page import="com.esd.model.data.UserGroup"%>
<%@page import="com.esd.model.data.persisted.SystemUser"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script>
/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */

function dropDown(dropdownId) {
  document.getElementById(dropdownId).classList.toggle("show");
}

</script>
<% SystemUser uiCurrentUser = (SystemUser)(session.getAttribute("currentSessionUser"));%>
<div class="sidebar_container">
    <a href="${pageContext.request.contextPath}/dashboard">
        <img class="logo" src="${pageContext.request.contextPath}/res/images/logo_white.png" alt="SmartWare Logo">

    </a>
    <nav id="sidebar">
        <ul class="list-unstyled components">
            <li>
                <a href="${pageContext.request.contextPath}/dashboard"><i class="fa fa-home nav-icons"></i>Dashboard</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/logout"><i class="fa fa-undo nav-icons"></i>Logout</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/<% out.print(request.getSession().getAttribute("previousPage")); %> ">
                    <i class="fa fa-arrow-circle-left nav-icons"></i>Back</a>
            </li>
            <br><br>
        <% if(uiCurrentUser.getUserGroup().equals(UserGroup.ADMIN) || uiCurrentUser.getUserGroup().equals(UserGroup.RECEPTIONIST)) { %>
            <li>
                <a data-toggle="collapse" class="dropdown-toggle dropdown-btn" onclick="dropDown('appointmentSubMenu')" href="#">Appointments</a>
                <ul id="appointmentSubMenu" class="list-unstyled dropdown-container">
                    <li>
                        <a href="${pageContext.request.contextPath}/appointments/book">Book An Appointment</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/appointments/schedule">Schedule</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/invoices/search">Invoices</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/reports">Reports</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/settings">System Settings</a>
            </li>
            <li>
                 <a data-toggle="collapse" class="dropdown-toggle dropdown-btn" onclick="dropDown('userSubMenu')" href="#">User</a>
                 <ul id="userSubMenu" class="list-unstyled dropdown-container">
                    <li>
                        <a href="${pageContext.request.contextPath}/users/manage">Manage Users</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/users/search">Search Users</a>
                    </li>
                </ul>
            </li>
        <% } else if(uiCurrentUser.getUserGroup().equals(UserGroup.DOCTOR) || uiCurrentUser.getUserGroup().equals(UserGroup.NURSE)) { %>
            <li>
                <a data-toggle="collapse" class="dropdown-toggle dropdown-btn" onclick="dropDown('appointmentSubMenu')" href="#">Appointments</a>
                <ul id="appointmentSubMenu" class="list-unstyled dropdown-container">
                    <li>
                        <a href="${pageContext.request.contextPath}/appointments/book">Book An Appointment</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/appointments/schedule">Schedule</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/invoices/search">Invoices</a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/admin/reports">Reports</a>
            </li>
            <li>
                 <a data-toggle="collapse" class="dropdown-toggle dropdown-btn" onclick="dropDown('userSubMenu')" href="#">User</a>
                 <ul id="userSubMenu" class="list-unstyled dropdown-container">
                    <li>
                        <a href="${pageContext.request.contextPath}/users/search">Search Users</a>
                    </li>
                </ul>
            </li>
        <% } else if(uiCurrentUser.getUserGroup().equals(UserGroup.PRIVATE_PATIENT) || uiCurrentUser.getUserGroup().equals(UserGroup.NHS_PATIENT)) { %>
            <li>
                <a data-toggle="collapse" class="dropdown-toggle dropdown-btn" onclick="dropDown('appointmentSubMenu')" href="#">Appointments</a>
                <ul id="appointmentSubMenu" class="list-unstyled dropdown-container">
                    <li>
                        <a href="${pageContext.request.contextPath}/appointments/book">Book An Appointment</a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/appointments/schedule">Schedule</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/invoices/search">Invoices</a>
            </li>
        <% } %>
        </ul>
    </nav>
</div>