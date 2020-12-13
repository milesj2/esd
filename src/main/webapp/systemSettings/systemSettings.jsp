<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>System Settings</title>
</head>
    <body>
        <form method="post" action="${pageContext.request.contextPath}/systemSettings">
            <h4>Consultation fees (£/h) for doctors and nurses</h4>
            <label>Doctor fee (default £150.00/h): </label>
            <input type="text" placeholder="Enter fee (£/h)" name=<%=DaoConsts.SYSTEMSETTING_FEE_DOCTOR%> id=<%=DaoConsts.SYSTEMSETTING_FEE_DOCTOR%>>
            <label>Current: <%=request.getAttribute(DaoConsts.SYSTEMSETTING_FEE_DOCTOR)%></label><br>

            <label>Nurse fee (default £100.00/h): </label>
            <input type="text" placeholder="Enter fee (£/h)" name=<%=DaoConsts.SYSTEMSETTING_FEE_NURSE%> id=<%=DaoConsts.SYSTEMSETTING_FEE_NURSE%>>
            <label>Current: <%=request.getAttribute(DaoConsts.SYSTEMSETTING_FEE_NURSE)%></label><br>

            <h4>Consultation slot time (default 10 minutes)</h4>
            <label>Consultation slot time (minutes): </label>
            <input type="text" placeholder="Enter slot time (minutes)" name=<%=DaoConsts.SYSTEMSETTING_SLOT_TIME%> id=<%=DaoConsts.SYSTEMSETTING_SLOT_TIME%>>
            <label>Current: <%=request.getAttribute(DaoConsts.SYSTEMSETTING_SLOT_TIME)%></label><br>

            <button value="update">Update</button>
            <% if (request.getAttribute("result") != null) { %>
            	<div><strong><%=request.getAttribute("result")%></strong></div>
            <% } %>
        </form>
    </body>
</html>
