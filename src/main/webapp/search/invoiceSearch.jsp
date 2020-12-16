<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Invoice" %>

<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice Search</title>
</head>
<body>
<h2>Invoice Search page</h2>
<h3>Enter your search terms to retrieve the invoice details</h3>
<div>
    <form method="post" action="${pageContext.request.contextPath}/invoiceSearch">
        <table border="1" cellpadding="5">
            <tr>
                <th>Id</th>
                <th>Invoice Date</th>
                <th>Invoice Time</th>
                <th>Invoice Status</th>
                <th>Employee Id</th>
                <th>Patient Id</th>
                <th>Appointment Id</th>
                <th>Actions</th>
            </tr>
            <tr>
                <td><input type="text" name=<%=DaoConsts.ID%> size="10" /></td>
                <td><input type="date" name=<%=DaoConsts.INVOICE_DATE%> size="10" /></td>
                <td><input type="time" name=<%=DaoConsts.INVOICE_TIME%> size="10" /></td>
                <td><input type="text" name=<%=DaoConsts.INVOICE_STATUS%> size="10" /></td>
                <td><input type="text" name=<%=DaoConsts.EMPLOYEE_ID_FK%> size="10" /></td>
                <td><input type="text" name=<%=DaoConsts.PATIENT_ID_FK%> size="10" /></td>
                <td><input type="text" name=<%=DaoConsts.APPOINTMENT_ID_FK%> size="10" /></td>
                <td>
                </td>
            </tr>
            <tr>
                <input type="submit" value="Search" />
            </tr>
            <% try {
                ArrayList<Invoice> invoiceList = (ArrayList<Invoice>)request.getAttribute("table");
                for(Invoice invoice:invoiceList){ %>
            <tr>
                <td><%=invoice.getId()%></td>
                <td><%=invoice.getInvoiceDate()%></td>
                <td><%=invoice.getInvoiceTime()%></td>
                <td><%=invoice.getInvoiceStatus()%></td>
                <td><%=invoice.getEmployeeId()%></td>
                <td><%=invoice.getPatientId()%></td>
                <td><%=invoice.getAppointmentId()%></td>
                <td><a href='invoice?id=<%=invoice.getId()%>'>Search Invoice</a></td>
            </tr>
            <% }
            } catch(Exception e){
            } %>
        </table>
    </form>
</div>
</body>
</html>
