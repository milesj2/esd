<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)(request.getAttribute("editUser"));
    String[] userGroups = Arrays.toString(UserGroup.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../../css/simple-switch.css">
    <title>Admin | Edit User</title>
</head>
<body>

<%
    if (user.getUserGroup() == UserGroup.ADMIN) { %>
        <h1>Editing User is disabled for admin accounts from this page.</h1>
        <a href="manage.jsp">Return to User Management</a>
    <%  return;
    }
    %>
    <h1>Editing User <% out.print(user.getUsername()); %></h1>
    <h2>Name: <% out.print(user.getUserDetails().getFirstName()); %></h2>
    <button onclick="window.location = '${pageContext.request.contextPath}/user/manage';">Cancel</button>
    <form method="post" action="${pageContext.request.contextPath}/user/edit" id="userEdit">
        <input type="submit" name="saveEdit" value="Save">
        <br>
        <input type="hidden" name="id" value="<% out.print(user.getId()); %>">
        <label for="username">Username</label>
        <input id="username" name="username" type="text" value="<% out.print(user.getUsername()); %>">
        <br>
        <label for="password">Password</label>
        <input id="password" name="password" type="text" value="<% out.print(user.getPassword()); %>">
        <br>
        <label for="firstname">First Name</label>
        <input id="firstname" name="firstname" type="text" value="<% out.print(user.getUserDetails().getFirstName()); %>">
        <br>
        <label for="lastname">Second Name</label>
        <input id="lastname" name="lastname" type="text" value="<% out.print(user.getUserDetails().getLastName()); %>">
        <br>
        <label for="addressline1">Adress Line 1</label>
        <input id="addressline1" name="addressline1" type="text" value="<% out.print(user.getUserDetails().getAddressLine1()); %>">
        <br>
        <label for="addressline2">Address Line 2</label>
        <input id="addressline2" name="addressline2" type="text" value="<% out.print(user.getUserDetails().getAddressLine2()); %>">
        <br>
        <label for="addressline3">Address Line 3</label>
        <input id="addressline3" name="addressline3" type="text" value="<% out.print(user.getUserDetails().getAddressLine3()); %>">
        <br>
        <label for="town">Town</label>
        <input id="town" name="town" type="text" value="<% out.print(user.getUserDetails().getTown()); %>">
        <br>
        <label for="postcode">Postcode</label>
        <input id="postcode" name="postcode" type="text" value="<% out.print(user.getUserDetails().getPostCode()); %>">
        <br>
        <label for="dob">Date of Birth</label>
        <input id="dob" name="dob" type="date" value="<% out.print(user.getUserDetails().getDateOfBirth()); %>">
        <br>
        <label for="usergroup">User Group</label>
        <select name="usergroup" id="usergroup" form="userEdit">
            <%
                for(String usergroup:userGroups){
                    out.print("<option value='" + usergroup + "' ");
                    if (usergroup.equals(user.getUserGroup().name())){
                        out.print("selected='selected'");
                    }
                    out.print(">" + usergroup + "</option>");
                }
            %>
        </select>
        <br>
        <label>Account Active</label>
        <br>
        <label class="switch">
            <input name="active" type="checkbox" class="switch-input" <% if(user.isActive()) out.print("checked");%>>
            <span class="switch-label" data-on="on" data-off="off"></span>
            <span class="switch-handle"></span>
        </label>
    </form>

</body>
</html>