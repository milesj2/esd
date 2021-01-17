<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>

<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Search</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
 <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
    <main>
                <h2>User Search page</h2>
                <h3>Enter your search terms to retrieve systemUser details</h3>
                    <table border="1" cellpadding="5" class="search_table">
                        <tr>
                            <th>Id</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Address</th>
                            <th>Town</th>
                            <th>Post Code</th>
                            <th>Dob</th>
                            <th sort="lock">Actions</th>
                        </tr>
                        <form method="post" action="${__SELF}">
                            <tr>
                                <td><input type="text" name=<%=DaoConsts.ID%> size="10" /></td>
                                <td><input type="text" name=<%=DaoConsts.USERDETAILS_FIRSTNAME%> size="10" /></td>
                                <td><input type="text" name=<%=DaoConsts.USERDETAILS_LASTNAME%> size="10" /></td>
                                <td><input type="text" name=<%=DaoConsts.USERDETAILS_ADDRESS1%> size="10" /></td>
                                <td><input type="text" name=<%=DaoConsts.USERDETAILS_TOWN%> size="10" /></td>
                                <td><input type="text" name=<%=DaoConsts.USERDETAILS_POSTCODE%> size="10" /></td>
                                <td><input type="date" name=<%=DaoConsts.USERDETAILS_DOB%> size="10" /></td>
                                <td>
                                </td>
                            </tr>
                            <tr>
                                <input type="submit" name="search" value="search" />
                                <% if (request.getParameter("redirect") != null){%>
                                <input type="submit" name="cancel" value="cancel" />
                                <% } %>

                                <input type="hidden" name="type" value="search" />
                            </tr>
                        </form>
                        <% try {
                            ArrayList<UserDetails> userDetailList = (ArrayList<UserDetails>)request.getAttribute("table");
                            for(UserDetails userDetails:userDetailList){ %>
                        <tr>
                            <td><%=userDetails.getUserId()%></td>
                            <td><%=userDetails.getFirstName()%></td>
                            <td><%=userDetails.getLastName()%></td>
                            <td><%=userDetails.getAddressLine1()%>
                                <%=userDetails.getAddressLine2()%>
                                <%=userDetails.getAddressLine3()%></td>
                            <td><%=userDetails.getTown()%></td>
                            <td><%=userDetails.getPostCode()%></td>
                            <td><%=userDetails.getDateOfBirth()%></td>
                            <td>
                                <% if (request.getParameter("redirect") != null){%>
                                <form method="post" action="${__SELF}">
                                    <input type="hidden" name="type" value="result" />
                                    <input type="hidden" name="selectedUserId" value="<%=userDetails.getUserId()%>" />
                                    <input type="submit" value="Select User" />
                                </form>
                                <%}else{%>
                                <a href='${pageContext.request.contextPath}/users/edit?id=<%=userDetails.getUserId()%>'>Search Users</a>
                                <%}%>
                            </td>
                        </tr>
                        <% }
                        } catch(Exception e){
                        } %>
                    </table>
                </main>
    </div>
<script>
    var contextPath = "${pageContext.request.contextPath}"
    addFuncToTableControl();
</script>
</body>
</html>

