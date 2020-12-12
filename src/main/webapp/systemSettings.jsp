<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import = "java.util.Date" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>System Settings</title>
    <script type="text/javascript" src="jquery-1.2.6.min.js"></script> 
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="css/master.css">
    <%
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String max_date = sdf.format(new Date());
    %>
</head>
    <body class="system-settings container">
        <img src="images/logo.png" class="loginLogo" alt="" />
        <h1>System Settings</h1>
        <p>Modify:</p>
        <hr>
        <form action="ModifySettings" name="modifySettings" method="post">
            <h4>Consultation fees (£/h) for doctors and nurses</h4>
            <label for="baseConsultationFeeDoctor">Doctor fee: *</label>
            <input type="text" placeholder="Please enter fee (£/h)" name="baseConsultationFeeDoctor" id="baseConsultationFeeDoctor" required><br>

            <label for="baseConsultationFeeNurse">Nurse fee: *</label>
            <input type="text" placeholder="Please enter fee (£/h)" name="baseConsultationFeeNurse" id="baseConsultationFeeNurse" required><br>

            <h4>Consultation slot time (default 10 mins)</h4>
            <label for="consultationSlotTime">Consultation slot time (mins): *</label>
            <input type="text" placeholder="Please enter slot time (mins)" name="consultationSlotTime" id="consultationSlotTime" required><br>

            <button class="btn" value="Update" id="update" disabled>Update</button>
            <% if (request.getAttribute("success").equals("success")) { %>
            	<div class="msg"><strong>Success!</strong></div>
            <% } else { %>
            	<div class="msg"><strong>Error: failed to save.</strong></div>
            <% } %>
        </form>        
    </body>
</html> 