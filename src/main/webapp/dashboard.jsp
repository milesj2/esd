<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User currentUser = (User)(session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>User Dashboard</title>
    <link rel="stylesheet" href="res/css/master.css">
    <script src="res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="res/components/titlebar.jsp" %>
            <main>
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
                <a href='appointments/schedule'>Appointments</a>
                <br>
                <a href='admin/settings'>Manage System Settings</a>
                <%}%>
            </main>
        </div>
    </div>
</body>
</html>
