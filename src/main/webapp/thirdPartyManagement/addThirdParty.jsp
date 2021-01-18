<%@page import="com.esd.model.data.ThirdPartyType" %>
<%@page import="com.esd.model.dao.DaoConsts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Third Party</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
            <h2>Add New Third Party</h2>
            <a href='${pageContext.request.contextPath}/thirdPartyManagement/manage'><button>Manage Third Party</button></a>
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
            </main>
        </div>    
    </div>        
</body>
</html>
