<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.controller.authentication.LoginController" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% User currentUser = (User)(session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="css/master.css">
</head>
<body>
    <div class="container">

    <% if(currentUser == null){%>
        <div class="loginFormWrapper">
            <img src="images/logo.png" class="loginLogo" alt="" />
            <form action="login" method="POST">
                <input type="text" placeholder="username" id="username" name="username" val=""/><br/>
                <input type="password" placeholder="password" id="password" name="password" val=""/><br/>
                <button type="submit">Login</button>
                <span class="errMessage"><% if(request.getParameter("err") != null){
                    out.print("Invalid username/password");
                } %></span>
            </form>
            <div class="noAccount">
                <h1>Don't have an account?</h1>
                <a href="#">Create account</a>
            </div>
        </div>
    <% }else{ %>
        Redirects are disabled in your browser please enable them to continue;
        <% response.sendRedirect("dashboard.jsp"); %>
    <% } %>
    </div>
</body>
</html>
