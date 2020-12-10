<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>

<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Search</title>
</head>
<body>
<h2>User Search page</h2>
<h3>Enter your search terms to retrieve user details</h3>
<div>
    <form method="post" action="${pageContext.request.contextPath}/userSearchForm">
        <table border="1" cellpadding="5">
            <tr>
                <th>Id</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Address</th>
                <th>Town</th>
                <th>Post Code</th>
                <th>Dob</th>
                <th>Actions</th>
            </tr>
            <tr>
                <td><input type="text" name=<%=DaoConsts.USERDETAILS_ID%> size="10" /></td>
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
                <input type="submit" value="Search" />
            </tr>
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
                    <td><%=userDetails.getDOB()%></td>
                    <td><a href='userpage?id=<%=userDetails.getUserId()%>'>Search Users</a></td>
                </tr>
                <% }
            } catch(Exception e){
            } %>
        </table>
    </form>
</div>
</body>
</html>

