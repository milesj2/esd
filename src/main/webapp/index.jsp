<%@ page import="com.esd.views.LoginErrors" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="css/master.css">
</head>
<body>
    <div class="container">
        <div class="loginFormWrapper">
            <img src="images/logo.png" class="loginLogo" alt="" />
            <form action="login" method="POST">
                <input type="text" placeholder="username" id="username" name="username" val=""/><br/>
                <input type="password" placeholder="password" id="password" name="password" val=""/><br/>
                <button type="submit">Login</button>
                <span class="errMessage">
                    <% if (request.getAttribute("errorMessage") != null){%>
                        <%= request.getAttribute("errorMessage") %>
                    <% } %>
                </span>
            </form>
            <div class="noAccount">
                <h1>Don't have an account?</h1>
                <a href='register'>Create account</a>
            </div>
        </div>
    </div>
</body>
</html>
