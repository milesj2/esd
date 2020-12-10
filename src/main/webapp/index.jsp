<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.controller.authentication.LoginController" %>
<%@ page import="com.esd.views.ViewsConsts" %>

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

    <% if(currentUser != null){
        out.print(ViewsConsts.Error.REDIRECTS_DISABLED);
        response.sendRedirect("dashboard.jsp");
        return;
    } %>
        <div class="loginFormWrapper">
            <img src="images/logo.png" class="loginLogo" alt="" />
            <form action="login" method="POST">
                <input type="text" placeholder="username" id="username" name="username" val=""/><br/>
                <input type="password" placeholder="password" id="password" name="password" val=""/><br/>
                <button type="submit">Login</button>
                <span class="errMessage">
                    <%  String err = request.getParameter("err");
                        String errMsg;
                        if(err != null){
                            int errCode;
                            try {
                                errCode = Integer.parseInt(err);
                            } catch (NumberFormatException e){
                                errCode = -1;
                            }
                            switch (errCode){
                                case 1:
                                    errMsg = "Invalid username/password";
                                    break;
                                case 2:
                                    errMsg = "This account is locked. Please contact your admin to reactive the account.";
                                    break;
                                default:
                                    errMsg = "Unknown Error! Please contact admin if this error continues";
                                    break;
                            }
                            out.print(errMsg);
                        } %>
                </span>
            </form>
            <div class="noAccount">
                <h1>Don't have an account?</h1>
                <a href="#">Create account</a>
            </div>
        </div>
    </div>
</body>
</html>
