<%@page import="com.esd.model.data.InvoiceStatus"%>
<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Invoice" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% SystemUser currentSystemUser = (SystemUser)(session.getAttribute("currentSessionUser"));%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice Search</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <h2>Invoice Search page</h2>
            <h3>Enter your search terms to retrieve the invoice details</h3>
            <%  String msg = request.getParameter("msg");
            if (msg != null){%>
            <strong><% out.print(msg);%></strong><br>
            <%} %><br>

                <table border="1" cellpadding="5" class="search_table">
                    <tr>
                        <th>Id</th>
                        <th>Invoice Date</th>
                        <th>Invoice Time</th>
                        <th>Invoice Status</th>
                        <th>Employee Id</th>
                        <th>Patient Id</th>
                        <th>Appointment Id</th>
                        <th sort="lock">Actions</th>
                        <th sort="lock">Payment</th>
                    </tr>
                    <tr>
                        <form method="post" action="${pageContext.request.contextPath}/invoices/search">
                                <input type="submit" name="search" value="Search" />
                                    <% if (request.getParameter("redirect") != null){%>
                                <input type="submit" name="cancel" value="cancel" />
                                    <% } %>
                                <input type="hidden" name="type" value="search" />
                            <td><input type="text" name=<%=DaoConsts.ID%> size="10" /></td>
                            <td><input type="date" name=<%=DaoConsts.INVOICE_DATE%> size="10" /></td>
                            <td><input type="time" name=<%=DaoConsts.INVOICE_TIME%> size="10" /></td>
                            <td><input type="text" name=<%=DaoConsts.INVOICE_STATUS%> size="10" /></td>
                            <td><input type="text" name=<%=DaoConsts.EMPLOYEE_ID_FK%> size="10" /></td>
                            <td><input type="text" name=<%=DaoConsts.PATIENT_ID_FK%> size="10" /></td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_ID_FK%> size="10" /></td>
                            <td></td>
                            <td></td>
                        </form>
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
                        <td>
                            <% if (request.getParameter("redirect") != null){%>
                            <form method="post" action="${__SELF}">
                                <input type="hidden" name="type" value="result" />
                                <input type="hidden" name="selectedInvoiceId" value="<%=invoice.getId()%>" />
                                <input type="submit" value="Select Invoice" />
                            </form>
                            <%}else{%>
                            <a href='${pageContext.request.contextPath}/invoices/edit?id=<%=invoice.getId()%>'>Edit Details</a>
                            <%}%>
                        </td>
                        <td>
                            <% if (request.getParameter("redirect") != null){%>
                            <form method="post" action="${__SELF}">
                                <input type="hidden" name="type" value="result" />
                                <input type="hidden" name="selectedInvoiceId" value="<%=invoice.getId()%>" />
                                <input type="submit" value="Select Invoice" />
                            </form>
                            <%}else{%>
                            <a href='${pageContext.request.contextPath}/invoices/pay?selectedInvoiceId=<%=invoice.getId()%>'>View/Pay</a>
                            <%}%>
                        </td>
                    </tr>
                    <% }
                    } catch(Exception e){
                    } %>
                </table>
            </form>
        </main>
    </div>
</div>
<script>
    var contextPath = "${pageContext.request.contextPath}"
    addFuncToTableControl();
</script>
</body>
</html>
