<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% SystemUser currentSystemUser = (SystemUser)(session.getAttribute("currentSessionUser"));%>
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
                Welcome <% out.print(currentSystemUser.getUsername()); %>. Your privilege level is <% out.print(currentSystemUser.getUserGroup().name()); %>
                <br>
                <div id="dashboard" class="widgets">
                </div>
            </main>
        </div>
    </div>
<script>
    var items;

    // PLACE HOLDER GENERATION
    <% if(currentSystemUser.getUserGroup().equals(UserGroup.ADMIN) || currentSystemUser.getUserGroup().equals(UserGroup.RECEPTIONIST)) { %>
     items = [
        { name: "Manage Users", link: "users/manage", icon: "res/icons/users.png" },
        { name: "Search Users", link: "users/search", icon: "res/icons/user-search.png" },
        { name: "Admin Reports", link: "admin/reports", icon: "res/icons/clipboard.png" },
        { name: "Search Invoices", link: "invoices/search", icon: "res/icons/receipt-search.png" },
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/calendar.png" },
        { name: "Manage System Settings", link: "admin/settings", icon: "res/icons/settings.png"},
    ];
    <% } else if(currentSystemUser.getUserGroup().equals(UserGroup.DOCTOR)) { %>
    items = [
        { name: "Search Users", link: "users/search", icon: "res/icons/search.png" },
        { name: "Admin Reports", link: "admin/reports", icon: "res/icons/clipboard.png" },
        { name: "Search Invoices", link: "invoices/search", icon: "res/icons/search.png" },
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/calendar.png" },
    ];
    <% } else if(currentSystemUser.getUserGroup().equals(UserGroup.NURSE)) { %>
    items = [
        { name: "Search Users", link: "users/search", icon: "res/icons/user-search.png" },
        { name: "Admin Reports", link: "admin/reports", icon: "res/icons/clipboard.png" },
        { name: "Search Invoices", link: "invoices/search", icon: "res/icons/receipt-search.png" },
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/calendar.png" },
    ];
    <% } else if(currentSystemUser.getUserGroup().equals(UserGroup.PRIVATE_PATIENT)) { %>
    items = [
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/calendar.png" },
    ];
    <% } else if(currentSystemUser.getUserGroup().equals(UserGroup.NHS_PATIENT)) { %>
    items = [
        { name: "Appointments", link: "appointments/schedule", icon: "res/icons/calendar.png" },
    ];
    <% } %>

    generateWidgets(items);

</script>

</body>
</html>
