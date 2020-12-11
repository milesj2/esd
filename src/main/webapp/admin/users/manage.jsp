<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.data.UserGroup" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>USER MANAGEMENT PAGE</h1>
<p style="color:Red">
    <%  String errMsg = request.getParameter("errMsg");
        if (errMsg != null){
            out.print(errMsg);
        } %>
</p>
<a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a>
<table>
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Password</th>
        <th>User Group</th>
        <th>User Details</th>
        <th>Manage</th>
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
                out.print(String.format("<td><a href='edit?id=%d'>Edit</a></td>", user.getId()));
            } else {
                out.print("<td><p>Editing from this page is disabled for admin account</p></td>");
            }

            out.print("</tr>");
        }
    %>
</table>


</body>
</html>