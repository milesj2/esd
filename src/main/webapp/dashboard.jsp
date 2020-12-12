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
        <% if(currentUser.getUserGroup() == UserGroup.ADMIN) {
            out.print("<a href='user/manage'>Manage Users</a>");
        }%>
    </div>

    <div>
        <% if(currentUser.getUserGroup() == UserGroup.ADMIN) { // todo change for appropriate user group
            out.print("<a href='userSearch'>Search Users</a>");
        }%>
    </div>
    <div>
        <% if(currentUser.getUserGroup() == UserGroup.ADMIN) { // todo change for appropriate user group
            out.print("<a href='admin/reports'>AdminReports</a>");
        }%>
    </div>

    <div>
        <% if(currentUser.getUserGroup() == UserGroup.ADMIN) { // todo change for appropriate user group
            out.print("<a href='invoiceSearch'>Search Invoices</a>");
        }%>
    </div>
</body>
</html>
