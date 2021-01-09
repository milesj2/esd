<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.data.UserGroup" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Users</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <script>var contextPath = "${pageContext.request.contextPath}"</script>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
                <h1>USER MANAGEMENT PAGE</h1>
                <p style="color:Red">
                    <%  String errMsg = request.getParameter("errMsg");
                        if (errMsg != null){
                            out.print(errMsg);
                        } %>
                </p>
                <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
                <a href="${pageContext.request.contextPath}/<% out.print(request.getAttribute("previousPage")); %>">BACK</a>
                <a href="${pageContext.request.contextPath}<% out.print(request.getAttribute("currentPage")); %>">CURRENT</a>
                <table class="search_table">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Password</th>
                        <th>User Group</th>
                        <!--<th>User Details</th> -->
                        <th sort="lock">Manage</th>
                    </tr>
                    <%

                        ArrayList<User> users = (ArrayList<User>) (request.getAttribute("users"));

                        for (User user:users){
                            out.print("<tr>");
                            out.print(String.format("<td>%d</td>", user.getId()));
                            out.print(String.format("<td>%s</td>", user.getUsername()));
                            out.print(String.format("<td>%s</td>", user.getPassword()));
                            out.print(String.format("<td>%s</td>", user.getUserGroup()));
                            if (user.getUserGroup() != UserGroup.ADMIN){
                                out.print(String.format("<td><a href='edit?id=%d'><img src='%s/res/icons/edit.png'/></a></td>", user.getId(), request.getContextPath()));
                            } else {
                                out.print("<td><p>Editing from this page is disabled for admin account</p></td>");
                            }

                            out.print("</tr>");
                        }
                    %>
                </table>
            </main>
        </div>
    </div>
<script>addFuncToTableControl();</script>
</body>
</html>
