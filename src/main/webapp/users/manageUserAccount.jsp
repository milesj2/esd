<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>

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

                        ArrayList<SystemUser> systemUsers = (ArrayList<SystemUser>) (request.getAttribute("systemUsers"));

                        for (SystemUser systemUser : systemUsers){
                            out.print("<tr>");
                            out.print(String.format("<td>%d</td>", systemUser.getId()));
                            out.print(String.format("<td>%s</td>", systemUser.getUsername()));
                            out.print(String.format("<td>%s</td>", systemUser.getPassword()));
                            out.print(String.format("<td>%s</td>", systemUser.getUserGroup()));
                            if (systemUser.getUserGroup() != UserGroup.ADMIN){
                                out.print(String.format("<td><a href='edit?id=%d'><img src='%s/res/icons/edit.png'/></a></td>", systemUser.getId(), request.getContextPath()));
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
<script>
    var contextPath = "${pageContext.request.contextPath}"
    addFuncToTableControl();
</script>
</body>
</html>
