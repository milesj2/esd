<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    SystemUser systemUser = (SystemUser)(request.getAttribute("editUser"));
    String[] userGroups = Arrays.toString(UserGroup.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../res/css/simple-switch.css">
    <title>Admin | Edit User</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
        <%
            if (systemUser.getUserGroup() == UserGroup.ADMIN) { %>
                <h1>Editing User is disabled for admin accounts from this page.</h1>
                <a href="${pageContext.request.contextPath}/systemUsers/manage">Return to User Management</a>
            <%  return;
            }
            %>
            <h1>Editing User <%= systemUser.getUsername() %></h1>
            <h2>Name: <%= systemUser.getUserDetails().getFirstName() %></h2>
            <button class="input_form_button" onclick="window.location='${pageContext.request.contextPath}/systemUsers/manage';">Cancel</button>
            <form class="input_form" method="post" action="${pageContext.request.contextPath}/systemUsers/edit" id="userEdit">
                <input type="submit" name="saveEdit" value="Save">
                <br>
                <input type="hidden" name="id" value="<%= systemUser.getId() %>">
                <label for="username">Username</label>
                <input id="username" name="username" type="text" value="<%= systemUser.getUsername() %>">
                <br>
                <label for="password">Password</label>
                <input id="password" name="password" type="text" value="<%= systemUser.getPassword() %>">
                <br>
                <label for="firstname">First Name</label>
                <input id="firstname" name="firstname" type="text" value="<%= systemUser.getUserDetails().getFirstName() %>">
                <br>
                <label for="lastname">Second Name</label>
                <input id="lastname" name="lastname" type="text" value="<%=  systemUser.getUserDetails().getLastName() %>">
                <br>
                <label for="addressline1">Adress Line 1</label>
                <input id="addressline1" name="addressline1" type="text" value="<%= systemUser.getUserDetails().getAddressLine1() %>">
                <br>
                <label for="addressline2">Address Line 2</label>
                <input id="addressline2" name="addressline2" type="text" value="<%= systemUser.getUserDetails().getAddressLine2() %>">
                <br>
                <label for="addressline3">Address Line 3</label>
                <input id="addressline3" name="addressline3" type="text" value="<%= systemUser.getUserDetails().getAddressLine3() %>">
                <br>
                <label for="town">Town</label>
                <input id="town" name="town" type="text" value="<%= systemUser.getUserDetails().getTown() %>">
                <br>
                <label for="postcode">Postcode</label>
                <input id="postcode" name="postcode" type="text" value="<%= systemUser.getUserDetails().getPostCode() %>">
                <br>
                <label for="dob">Date of Birth</label>
                <input id="dob" name="dob" type="date" value="<%= systemUser.getUserDetails().getDateOfBirth() %>">
                <br>
                <label for="usergroup">User Group</label>
                <select name="usergroup" id="usergroup" form="userEdit">
                    <%
                        for(String usergroup:userGroups){
                            out.print("<option value='" + usergroup + "' ");
                            if (usergroup.equals(systemUser.getUserGroup().name())){
                                out.print("selected='selected'");
                            }
                            out.print(">" + usergroup + "</option>");
                        }
                    %>
                </select>
                <br>
                <label>Account Active</label>

                <label class="switch">
                    <input name="active" type="checkbox" class="switch-input" <% if(systemUser.isActive()) out.print("checked");%>>
                    <span class="switch-label" data-on="on" data-off="off"></span>
                    <span class="switch-handle"></span>
                </label>
            </form>
        </main>
    </div>
</body>
</html>