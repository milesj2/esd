<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Invoice" %>
<%@ page import="com.esd.model.data.InvoiceOptions" %>
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
                <form class="input_form" method="post" action="${pageContext.request.contextPath}/invoices/view">
                    <table>
                        <tr>
                            <td>Invoice id</td>
                            <td><input type="text" name=<%=DaoConsts.ID%> value="<%out.print(invoice.getId());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Date</td>
                            <td><input type="date" name=<%=DaoConsts.INVOICE_DATE%> value="<%out.print(invoice.getInvoiceDate());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Time</td>
                            <td><input type="time" name=<%=DaoConsts.INVOICE_TIME%> value="<%out.print(invoice.getInvoiceTime());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Status</td>
                            <td><input type="text" name=<%=DaoConsts.INVOICE_STATUS%> value="<%out.print(invoice.getInvoiceStatus());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Status change date</td>
                            <td><input type="date" name=<%=DaoConsts.INVOICE_STATUS_CHANGE_DATE%> value="<%out.print(invoice.getInvoiceStatusChangeDate());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Employee id</td>
                            <td><input type="text" name=<%=DaoConsts.EMPLOYEE_ID%> value="<%out.print(invoice.getEmployeeId());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Patient id</td>
                            <td><input type="text" name=<%=DaoConsts.PATIENT_ID%> value="<%out.print(invoice.getPatientId());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Private Patient?</td>
                            <td><input type="checkbox" name=<%=DaoConsts.PRIVATE_PATIENT%> value="<%out.print(invoice.isPrivatePatient());%>" size="10" /></td>
                        </tr>
                        <tr>
                            <td>Appointment id</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_ID_FK%> value="<%out.print(invoice.getAppointmentId());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Slots</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoice.getItems().get(0).getQuantity());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Cost</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoice.getItems().get(0).getCost());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Quantity</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoice.getItems().get(0).getQuantity());%>" size="10" required/></td>
                        </tr>
                        <tr>
                            <td>Description</td>
                            <td><input type="text" name=<%=DaoConsts.APPOINTMENT_SLOTS%> value="<%out.print(invoice.getItems().get(0).getDescription());%>" size="10" required/></td>
                        </tr>
                        <input name="option" type="radio" value=<%=InvoiceOptions.UPDATE%> required/>Update
                        <input name="option" type="radio" value=<%=InvoiceOptions.CREATE%> required/>Create
                        <button class="input_form_button" type="Submit" >Submit</button>
                    </table>
                </form>
                <% } catch (Exception e) { } %>
            </div>
        </main>
    </div>
</div>
</body>
</html>