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
            <label for="baseConsultationFeeDoctor">Doctor fee: *</label>
            <input type="text" placeholder="Please enter fee (£/h)" name=<%=DaoConsts.SYSTEMSETTING_FEE_DOCTOR%> id=<%=DaoConsts.SYSTEMSETTING_FEE_DOCTOR%> required><br>

            <label for="baseConsultationFeeNurse">Nurse fee: *</label>
            <input type="text" placeholder="Please enter fee (£/h)" name=<%=DaoConsts.SYSTEMSETTING_FEE_NURSE%> id=<%=DaoConsts.SYSTEMSETTING_FEE_NURSE%> required><br>

            <h4>Consultation slot time (default 10 minutes)</h4>
            <label for="consultationSlotTime">Consultation slot time (minutes): *</label>
            <input type="text" placeholder="Please enter slot time (minutes)" name=<%=DaoConsts.SYSTEMSETTING_SLOT_TIME%> id=<%=DaoConsts.SYSTEMSETTING_SLOT_TIME%> required><br>

            <button class="btn" value="Update" id="update" disabled>Update</button>
            <% if (request.getAttribute("success").equals("success")) { %>
            	<div class="msg"><strong>Success!</strong></div>
            <% } else { %>
            	<div class="msg"><strong>Error: failed to save.</strong></div>
            <% } %>
        </form>        
    </body>
</html> 