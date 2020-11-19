<%@ page import="com.esd.model.User" %>

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
        welcome to your dashboard <% currentUser.getUsername(); %>
    </div>

</body>
</html>
