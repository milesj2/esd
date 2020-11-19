<%@ page import="com.esd.model.User" %>
<%@ page import="com.esd.servlet.authentication.LoginServlet" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% User currentUser = (User)(session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <div>

    <% if(currentUser == null){%>
        <h1>Login</h1>
        <form action="login" method="POST">
            <label for="username">Username:</label>
            <input type="text" placeholder="username" id="username" name="username" val=""/>
            <label for="password">Password:</label>
            <input type="password" placeholder="password" id="password" name="password" val=""/>
            <button type="submit">Login</button>
            <span style="color:red;"><% if(request.getParameter("err") != null){
                out.print("Invalid username/password");
            } %></span>
        </form>
        <a href="#">Don't have an account? Signup</a>
    <% }else{ %>
        Redirects are disabled in your browser please enable them to continue;
        <% response.sendRedirect("dashboard.jsp"); %>
    <% } %>
    </div>
</body>
</html>
