<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User currentUser = (User)(session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>User Dashboard</title>
</head>
<body>
    <% if(currentUser == null) {
        response.sendRedirect("index.jsp");
        return; //return is safe and stops further script execution
    }%>

    <nav><a href="logout">Logout</a> </nav>
    <div>
        welcome to your dashboard <% out.print(currentUser.getUsername()); %> your privilege level is <% out.print(currentUser.getUserGroup().name()); %>
        <% if(currentUser.getUserGroup() == UserGroup.ADMIN) {
            out.print("<a href='admin/users/manage.jsp'>Manage Users</a>");
        }%>
    </div>

</body>
</html>
