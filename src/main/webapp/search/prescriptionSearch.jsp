<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Prescription" %>

<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Prescription Search</title>
</head>
<body>
<h2>Prescription Search Page</h2>
<h3>Enter your search terms to retrieve the prescription details</h3>
<div>
    <form method="post" action="${pageContext.request.contextPath}/prescriptionSearch">
        <table border="1" cellpadding="5">
            <tr>
                <th>Id</th>
                <th>Employee Id</th>
                <th>Patient Id</th>
                <th>Prescription Details</th>
                <th>Appointment Id</th>
                <th>Issue Date</th>
                <th>Actions</th>
            </tr>
            <tr>
                <td><input type="text" name=<%=DaoConsts.PRESCRIPTION_ID%> size="12" /></td>
                <td><input type="text" name=<%=DaoConsts.EMPLOYEE_ID%> size="12" /></td>
                <td><input type="text" name=<%=DaoConsts.PATIENT_ID%> size="12" /></td>
                <td><input type="text" name=<%=DaoConsts.PRESCRIPTION_DETAILS%> size="40" /></td>
                <td><input type="text" name=<%=DaoConsts.APPOINTMENT_ID%> size="12" /></td>
                <td><input type="text" name=<%=DaoConsts.PRESCRIPTION_ISSUE_DATE%> size="10" /></td>
                <td>
                </td>
            </tr>
            <tr>
                <input type="submit" value="Search" />
            </tr>
            <% try {
            ArrayList<Prescription> prescriptionList = (ArrayList<Prescription>)request.getAttribute("table");
            for(Prescription prescription:prescriptionList){ %>
                <tr>
                    <td><%=prescription.getId()%></td>
                    <td><%=prescription.getEmployeeId()%></td>
                    <td><%=prescription.getPatientId()%></td>
                    <td><%=prescription.getPrescriptionDetails()%></td>
                    <td><%=prescription.getAppointmentId()%></td>
                    <td><%=prescription.getIssueDate()%></td>
                    <td><a href='prescriptionPage?id=<%=prescription.getId()%>'>Search Prescription</a></td>
                </tr>
            <% }
            } catch(Exception e){
            } %>
        </table>
    </form>
</div>
</body>
</html>
