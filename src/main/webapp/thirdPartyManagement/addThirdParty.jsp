<%@page import="com.esd.model.data.ThirdPartyType"%>
<%@page import="com.esd.model.dao.DaoConsts"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Third Party</title>
    </head>
    <body class="add-third-party container">
        <img src="../res/images/logo.png" class="loginLogo" alt="" />
        <h1>Add Third Party</h1>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a><br>
        <a href='${pageContext.request.contextPath}/thirdPartyManagement/manage'>Manage Third Party</a>
        <p>Please fill in this form to add a third party.</p>
        <hr>
        <form action="${pageContext.request.contextPath}/thirdPartyManagement/add" name="addThirdParty" method="post">

            <h4>Third Party Details</h4>

            <label for="thirdPartyName">Name: *</label>
            <input type="text" placeholder="Enter Third Party Name" name="<%=DaoConsts.THIRDPARTY_NAME%>" id="thirdPartyName" required><br>

            <label for="addressline1">Street Address: *</label>
            <input type="text" placeholder="Enter first line of address" name="<%=DaoConsts.THIRDPARTY_ADDRESS1%>" id="addressline1" required><br>

            <label for="addressline2">Street Address Line 2:</label>
            <input type="text" placeholder="Enter second line of address" name="<%=DaoConsts.THIRDPARTY_ADDRESS2%>" id="addressline2"><br>

            <label for="addressline3">Street Address Line 3:</label>
            <input type="text" placeholder="Enter third line of address" name="<%=DaoConsts.THIRDPARTY_ADDRESS3%>" id="addressline3"><br>

            <label for="town">Town: *</label>
            <input type="text" placeholder="Enter your Town" name="<%=DaoConsts.THIRDPARTY_TOWN%>" id="town" required><br>

            <label for="postcode">Postcode: *</label>
            <input type="text" placeholder="Enter Postcode" name="<%=DaoConsts.THIRDPARTY_POSTCODE%>" id="postcode" required><br>
            
            <label for="type">Type:</label>
            <select id="ThirdPartyType" name="<%=DaoConsts.THIRDPARTY_TYPE%>">
              <option value="<%=ThirdPartyType.HOSPITAL%>">Hospital</option>
              <option value="<%=ThirdPartyType.SPECIALIST_CLINIC%>">Specialist Clinic</option>
            </select><br><br>

            <button type="Submit" class="btn btn-addThirdParty" value="Send" id="submit">Add</button>
            <% if ( request.getAttribute("notify") != null ) { %>
            <div class="registration_msg"><strong><%=request.getAttribute("notify")%></strong></div><br>
            <% } %>
        </form>
    </body>
</html>
