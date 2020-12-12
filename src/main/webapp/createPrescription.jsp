<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Prescription</title>
    <%
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String max_date = sdf.format(new Date());
    %>
</head>
    <body class="create-prescription-page container">
    
        <h1>Create Prescription</h1>
        <p>Please fill in this form to create a prescription.</p>
        <hr>
        <form action="${pageContext.request.contextPath}/PrescriptionCreateController" name="createPrescription" id="createPrescription" method="post">

            <label for="doctorFirstName">Doctor's First Name: </label>
            <input type="text" name="doctorFirstName" id="doctorFirstName" required><br>
            
            <label for="doctorLastName">Doctor's Last Name: </label>
            <input type="text" name="doctorLastName" id="doctorLastName" required><br>
            
            <label for="patientFirstName">Patient's First Name: </label>
            <input type="text" name="patientFirstName" id="patientFirstName" required><br>
            
            <label for="patientLastName">Patient's Last Name: </label>
            <input type="text" name="patientLastName" id="patientLastName" required><br>
            
            <label for="appointmentId">Appointment Id:</label>
            <input type="int"  name="appointmentId" id="appointmentId"><br>

            <label for="issueDate">Issue Date: </label>
            <input id="issueDate" name="issueDate" type="date" max="<%=max_date%>" required><br>
            
            <label for="prescriptionDetails">Prescription Details: </label><br>
            <textarea rows="4" cols="50" name="prescriptionDetails" form="createPrescription"></textarea><br>
            
            <button type="Submit" class="btn" value="Send" id="submit">Create Prescription</button>
            <% if ( request.getAttribute("notify") != null ) { %>
            <div class="registration_msg"><strong><%=request.getAttribute("notify")%></strong></div><br>
            <% } %>
        </form> 
        <hr>
    </body>
</html>