<%--
  Created by IntelliJ IDEA.
  User: Jordan
  Date: 05/12/2020
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% User currentUser = (User)(session.getAttribute("currentSessionUser"));%>
<html>
<head>
</head>
<body>
    <% if(currentUser == null) {
        response.sendRedirect("index.jsp");
    }%>

    <nav><a href="dashboard.jsp">Dashboard</a> <a href="logout">Logout</a> </nav>
</body>
</html>
