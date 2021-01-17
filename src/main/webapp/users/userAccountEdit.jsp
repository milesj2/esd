<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User)(request.getAttribute("editUser"));
    String[] userGroups = Arrays.toString(UserGroup.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My Account</title>
    <script src="../res/js/main.js"></script>
    <link rel="stylesheet" href="../res/css/master.css">
    <link rel="stylesheet" href="../res/css/simple-switch.css">
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <div class="user_container">
                <div class="container_header" onclick="onDropDownClick(this)">
                    <img src="../res/icons/chevron-right.png">
                    <h1>SmartWare Account</h1>
                </div>
                <form class="input_form toggle_hide" method="post" action="${pageContext.request.contextPath}/users/edit" id="userEdit">
                    <table>
                        <colgroup>
                            <col span="1" style="width: 200px;">
                            <col span="1" style="width: 220px;">
                        </colgroup>
                        <tr>
                            <td>
                                <label for="username">Username</label>
                            </td>
                            <td>
                                <input id="username" name="username" type="text" value="<%= user.getUsername() %>" disabled>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="password">Current Password</label>
                            </td>
                            <td>
                                <input id="password" name="password" type="password" value="">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="new_password">New Password</label>
                            </td>
                            <td>
                                <input id="new_password" name="new_password" type="password" value="" onchange="checkPasswords()">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="confirm_password">Confirm Password</label>
                            </td>
                            <td>
                                <input id="confirm_password" name="confirm_password" type="password" value="" onchange="checkPasswords()">
                            </td>
                        </tr>
                        <tr id="password_error_container" class="password_error_container toggle_hide">
                            <td colspan="2">
                                <p id="password_error"></p>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input id="password_submit" type="submit" name="saveEdit" value="Update" disabled>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="user_details_container">
                <div class="container_header" onclick="onDropDownClick(this)">
                    <img src="../res/icons/chevron-right.png">
                    <h1>Personal Details</h1>
                </div>
                <form class="input_form toggle_hide" method="post" action="${pageContext.request.contextPath}/users/edit" id="userDetailsEdit">
                    <table>
                        <tr>
                            <td>
                                <label for="firstname">First Name</label>
                            </td>
                            <td>
                                <input id="firstname" name="firstname" type="text" value="<%= user.getUserDetails().getFirstName() %>">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="lastname">Second Name</label>
                            </td>
                            <td>
                                <input id="lastname" name="lastname" type="text" value="<%=  user.getUserDetails().getLastName() %>">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="addressline1">Adress Line 1</label>
                            </td>
                            <td>
                                <input id="addressline1" name="addressline1" type="text" value="<%= user.getUserDetails().getAddressLine1() %>">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="addressline2">Address Line 2</label>
                            </td>
                            <td>
                                <input id="addressline2" name="addressline2" type="text" value="<%= user.getUserDetails().getAddressLine2() %>">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="addressline3">Address Line 3</label>
                            </td>
                            <td>
                                <input id="addressline3" name="addressline3" type="text" value="<%= user.getUserDetails().getAddressLine3() %>">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="town">Town</label>

                            </td>
                            <td>
                                <input id="town" name="town" type="text" value="<%= user.getUserDetails().getTown() %>">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="postcode">Postcode</label>
                            </td>
                            <td>
                                <input id="postcode" name="postcode" type="text" value="<%= user.getUserDetails().getPostCode() %>">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label for="dob">Date of Birth</label>
                            </td>
                            <td>
                                <input id="dob" name="dob" type="date" value="<%= user.getUserDetails().getDateOfBirth() %>">
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>
                                <input type="submit" name="saveEdit" value="Update">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </main>
    </div>
</div>
</body>
</html>
