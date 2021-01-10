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
                <h1>Dashboard</h1>
                Welcome <% out.print(currentUser.getUsername()); %>. Your privilege level is <% out.print(currentUser.getUserGroup().name()); %>
                <br>
                <div id="dashboard" class="widgets">

                </div>
            </main>
        </div>
    </div>
<script>
    var items;

    // PLACE HOLDER GENERATION
    <% if(currentUser.getUserGroup().equals(UserGroup.ADMIN) || currentUser.getUserGroup().equals(UserGroup.RECEPTIONIST)) { %>
     items = [
        { name: "Manage Users", link: "users/manage", icon: "res/icons/users.png" },
        { name: "Search Users", link: "users/search", icon: "res/icons/search.png" },
        { name: "Admin Reports", link: "admin/reports", icon: "res/icons/users.png" },
        { name: "Search Invoices", link: "invoices/search", icon: "res/icons/search.png" },
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/users.png" },
        { name: "Manage System Settings", link: "admin/settings", icon: "res/icons/search.png" },
    ];
    <% } else if(currentUser.getUserGroup().equals(UserGroup.DOCTOR)) { %>
    items = [
        { name: "Search Users", link: "users/search", icon: "res/icons/search.png" },
        { name: "Admin Reports", link: "admin/reports", icon: "res/icons/users.png" },
        { name: "Search Invoices", link: "invoices/search", icon: "res/icons/search.png" },
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/users.png" },
    ];
    <% } else if(currentUser.getUserGroup().equals(UserGroup.NURSE)) { %>
    items = [
        { name: "Search Users", link: "users/search", icon: "res/icons/search.png" },
        { name: "Admin Reports", link: "admin/reports", icon: "res/icons/users.png" },
        { name: "Search Invoices", link: "invoices/search", icon: "res/icons/search.png" },
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/users.png" },
    ];
    <% } else if(currentUser.getUserGroup().equals(UserGroup.PRIVATE_PATIENT)) { %>
    items = [
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/users.png" },
    ];
    <% } else if(currentUser.getUserGroup().equals(UserGroup.NHS_PATIENT)) { %>
    items = [
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/users.png" },
    ];
    <% } %>

    generateWidgets(items);

</script>

</body>
</html>
