<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.service.UserService" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.service.UserDetailsService" %>
<%@ page import="com.esd.model.exceptions.InvalidUserIDException" %>
<%@ page import="com.esd.views.ViewsConsts" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User currentUser = (User)(session.getAttribute("currentSessionUser"));
    if(currentUser == null){
        out.print("Redirects are disabled in your browser please enable them to continue");
        response.sendRedirect("../../index.jsp");
        return;
    } else if (currentUser.getUserGroup() != UserGroup.ADMIN){
        response.sendError(ViewsConsts.Error.HTML_UNAUTHORISED, "");
    }
%>


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
<a href="../../dashboard.jsp">Dashboard</a>
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
        try {
            ArrayList<User> users = UserService.getInstance().getUsers();

            for (User user:users){
                try {
                    user.setUserDetails(UserDetailsService.getInstance().getUserDetailsByUserID(user.getId()));
                } catch (InvalidUserIDException e) {
                    e.printStackTrace();
                }
                out.print("<tr>");
                out.print(String.format("<td>%d</td>", user.getId()));
                out.print(String.format("<td>%s</td>", user.getUsername()));
                out.print(String.format("<td>%s</td>", user.getPassword()));
                out.print(String.format("<td>%s</td>", user.getUserGroup()));
                if (user.getUserGroup() != UserGroup.ADMIN){
                    out.print(String.format("<td><a href='edit.jsp?id=%d'>Edit</a></td>", user.getId()));
                } else {
                    out.print("<td><p>Editing from this page is disabled for admin account</p></td>");
                }

                out.print("</tr>");
            }
        } catch (SQLException e){
            out.print(e.getMessage());
        }
    %>
</table>


</body>
</html>