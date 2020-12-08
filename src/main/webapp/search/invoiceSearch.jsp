<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.data.persisted.User" %>
<%@ page import="com.esd.model.data.persisted.Invoice" %>

<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User currentUser = (User)(session.getAttribute("currentSessionUser"));
    if(currentUser == null){
        response.sendRedirect("../index.jsp");
        return;
    }
    String[] userGroups = Arrays.toString(UserGroup.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice Search</title>
</head>
<body>
<h2>Invoice search page</h2>
<div>
    <form method="post" action="${pageContext.request.contextPath}/invoiceSearchForm">
        <table>
            <tr>
                <td><select name="SearchMatch">
                    <option value="All">-Select all-</option>
                    <option value="INVOICEID">Invoice ID</option>
                    <option value="INVOICEDATE">Invoice Date</option>
                    <option value="INVOICESTATUS">Invoice Status</option>
                    <option value="EMPOLOYEEID">Employee Id</option>
                    <option value="PATIENTID">Patient Id</option>
                </select></td>
                <td> is: </td>
                <td><input type="text" name="Search Term" size="10" /></td>
                <td><input type="submit" value="Search" /></td>
            </tr>
        </table>
    </form>
    <% try {
        ArrayList<Invoice> Invoices = (ArrayList<Invoice>)request.getAttribute("table"); %>
    <table border="1" cellpadding="5">
        <% if(Invoices.size() > 0) { // todo change for appropriate user group
            out.print("<tr>\n" +
                    "<th>ID</th>\n" +
                    "<th>Invoice Date</th>\n" +
                    "<th>Invoice Time</th>\n" +
                    "<th>Invoice Status</th>\n" +
                    "<th>Employee ID</th>\n" +
                    "<th>Patient ID</th>\n" +
                    "<th>Actions</th>\n" +
                    "</tr>");
        }
            for(Invoice invoice:Invoices){ %>
        <tr>
            <td><%=invoice.getId()%></td>
            <td><%=invoice.getInvoiceDate()%></td>
            <td><%=invoice.getInvoiceTime()%></td>
            <td><%=invoice.getInvoiceStatus()%>
            <td><%=invoice.getEmployeeId()%></td>
            <td><%=invoice.getPatientId()%></td>
            <td><a href='invoicepage?id=<%=invoice.getId()%>'>Search Users</a></td>
        </tr>
        <% } %>
    </table>
    <% } catch(Exception e){
    } %>
</div>
</body>
</html>