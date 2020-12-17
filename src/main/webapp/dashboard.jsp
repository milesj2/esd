<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User currentUser = (User)(session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>User Dashboard</title>
</head>
<body>
    <nav><a href="logout">Logout</a> </nav>
    <div>
        welcome to your dashboard <% out.print(currentUser.getUsername()); %> your privilege level is <% out.print(currentUser.getUserGroup().name()); %>
	    <br>
        <% if(currentUser.getUserGroup() == UserGroup.ADMIN) { %>
        <a href='users/manage'>Manage Users</a>
        <br>
        <a href='users/search'>Search Users</a>
        <br>
        <a href='admin/reports'>AdminReports</a>
        <br>
        <a href='invoices/search'>Search Invoices</a>
        <br>
        <a href='admin/settings'>Manage System Settings</a>
        <br>
        <a href='appointments/schedule'>Appointments</a>
        <%}%>
    </div>
</body>
</html>
