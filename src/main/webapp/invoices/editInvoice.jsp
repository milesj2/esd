<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Invoice" %>
<%@ page import="com.esd.model.data.persisted.InvoiceItem" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Invoice</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <h2>Manage Invoices</h2>
            <div>
                <div>
                    <% if(request.getAttribute("message") != null){ %>
                    <span class="errMessage"><%= request.getAttribute("message") %></span>
                    <% } %>
                </div>
                <% try { Invoice invoice = (Invoice)request.getAttribute("invoice"); %>
                <form class="input_form" method="post" action="${pageContext.request.contextPath}/invoices/edit">
                    <table>
                        <tr>
                            <td>Invoice id</td>
                            <td><input type="text" name=<%=DaoConsts.ID%> value="<%out.print(invoice.getId());%>" size="10" readonly required/></td>
                        </tr>
                        <tr>
                            <td>Date</td>
                            <td><input type="date" name=<%=DaoConsts.INVOICE_DATE%> value="<%out.print(invoice.getInvoiceDate());%>" size="10" readonly required/></td>
                        </tr>
                        <tr>
                            <td>Time</td>
                            <td><input type="time" name=<%=DaoConsts.INVOICE_TIME%> value="<%out.print(invoice.getInvoiceTime());%>" size="10" readonly required/></td>
                        </tr>
                        <tr>
                            <td>Status</td>
                            <td><input type="text" name=<%=DaoConsts.INVOICE_STATUS%> value="<%out.print(invoice.getInvoiceStatus());%>" size="10" readonly required/></td>
                        </tr>
                        <tr>
                            <td>Status change date</td>
                            <td><input type="date" name=<%=DaoConsts.INVOICE_STATUS_CHANGE_DATE%> value="<%out.print(invoice.getInvoiceStatusChangeDate());%>" size="10" readonly required/></td>
                        </tr>
                        <tr>
                            <td>Employee id</td>
                            <td><input type="text" name=<%=DaoConsts.EMPLOYEE_ID%> value="<%out.print(invoice.getEmployeeId());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Patient id</td>
                            <td><input type="text" name=<%=DaoConsts.PATIENT_ID%> value="<%out.print(invoice.getPatientId());%>" size="10" readonly required/></td>
                        </tr>
                        <tr>
                            <td>Private Patient?</td>
                            <td><input type="text" name=<%=DaoConsts.PRIVATE_PATIENT%> value="<%out.print(invoice.isPrivatePatient());%>" size="10" readonly /></td>
                        </tr>
                        <tr>
                            <td>Appointment id</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_ID_FK%> value="<%out.print(invoice.getAppointmentId());%>" size="10" required/></td>
                        </tr>
                        <% for(InvoiceItem invoiceItem: invoice.getItems()){%>
                        <tr>
                            <td>Slots</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoiceItem.getQuantity());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Cost</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoiceItem.getCost());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Quantity</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoiceItem.getQuantity());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Description</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoiceItem.getDescription());%>" size="10" required/></td>
                        </tr>
                        <%}%>
                        <tr>
                            <button class="input_form_button" type="Submit" >Update</button>
                        </tr>
                    </table>
                </form>
                <% } catch (Exception e) { } %>
            </div>
        </main>
    </div>
</div>
</body>
</html>