<%@page import="com.esd.model.data.persisted.Appointment"%>
<%@page import="com.esd.model.data.UserGroup"%>
<%@page import="com.esd.model.data.InvoiceStatus"%>
<%@page import="com.esd.model.data.persisted.InvoiceItem"%>
<%@page import="com.esd.model.data.persisted.SystemUser"%>
<%@page import="com.esd.model.data.persisted.UserDetails"%>
<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.Invoice" %>
<%@ page import="com.esd.model.data.InvoiceOptions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pay for Invoice</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>
    .search_table.invoice th {
      width: 200px;
    }
    </style>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <h2>Invoice Payment Information</h2>
            <a href='${pageContext.request.contextPath}/invoices/search'><button type="button">Back to Invoice Search</button></a><hr>
            <div class="row">
                
            <% try {
                Invoice invoice = (Invoice)request.getAttribute("invoice");
                InvoiceItem invoiceItem = (InvoiceItem)request.getAttribute("invoiceItem");
                SystemUser user = (SystemUser)(request.getAttribute("user"));
                SystemUser employee = (SystemUser)(request.getAttribute("employee"));
            %>
            
            <form method="post" action="${pageContext.request.contextPath}/invoices/pay?id=<%=invoice.getId()%>&uid=<%=invoice.getPatientId()%>&eid=<%=invoice.getEmployeeId()%>">
                <div class="invoice-info">
                    <h3>Invoice Details For Appointment No. <%out.print(invoice.getAppointmentId());%></h3>
                    <table border="1" cellpadding="5" class="search_table invoice">
                        <tr>
                            <th>Patient's Name </th>
                            <td><%= user.getUserDetails().getFirstName() %>
                                <%=  user.getUserDetails().getLastName() %></td>
                        </tr>
                        <tr>
                            <th>Full Address </th>
                            <td><%= user.getUserDetails().getAddressLine1() %>
                                <%= user.getUserDetails().getAddressLine2() %>
                                <%= user.getUserDetails().getAddressLine3() %><br>
                                <%= user.getUserDetails().getTown() %>, 
                                <%= user.getUserDetails().getPostCode() %></td>
                        </tr>
                        <tr>
                            <th>Date & Time</th>
                            <td><%out.print(invoice.getInvoiceDate());%> at 
                                <%out.print(invoice.getInvoiceTime());%></td>
                        </tr>
                        <tr>
                            <th>Appointment With </th>
                        <% if (employee.getUserGroup() != UserGroup.DOCTOR){%>
                            <td>Nurse</td>
                        </tr>
                        <tr>
                            <th>Nurse's Name </th>
                            <td><%= employee.getUserDetails().getFirstName() %>
                                <%= employee.getUserDetails().getLastName() %></td>
                        </tr>
                        <%}else{%>
                            <td>Doctor</td>
                        </tr>
                        <tr>
                            <th>Doctor's Name </th>
                            <td><%= employee.getUserDetails().getFirstName() %>
                                <%= employee.getUserDetails().getLastName() %></td>
                        </tr>
                        <%}%>
                        <tr>
                            <th>Private Patient?</th>
                            <td><%out.print(invoice.isPrivatePatient());%></td>
                        </tr>
                        <tr>
                            <th>Total Amount</th>
                            <td>£<%out.print(invoiceItem.getCost());%></td>
                        </tr>
                    </table>
                </div>
                <div class="invoice-pay">
                    <h3>Payment Details</h3>
                    <% if (invoice.getInvoiceStatus() != InvoiceStatus.PAID){%>
                    <label for="fname">Accepted Cards</label>
                    <div class="icon-container">
                      <i class="fa fa-cc-visa" style="color:navy;"></i>
                      <i class="fa fa-cc-amex" style="color:blue;"></i>
                      <i class="fa fa-cc-mastercard" style="color:red;"></i>
                      <i class="fa fa-cc-discover" style="color:orange;"></i>
                    </div><br>
                    <input type="text" name=<%=DaoConsts.ID%> value="<%out.print(invoice.getId());%>" size="10" hidden />
                    
                    <label for="cardname">Name on Card*</label>
                    <input type="text" id="cardname" name="cardname" placeholder="John More Doe" required><br>

                    <label for="cardnumber">Card number*</label>
                    <input type="text" id="cardnumber" name="cardnumber" maxlength="16" placeholder="1111-2222-3333-4444" required><br>

                    <label for="expiry">Expiry Date* (MM/YY)</label>
                    <input type="number" id="monthexpiry" name="monthexpiry" min="01" max="12" maxlength="2" placeholder="MM" required><span>/</span>
                    <input type="number" id="yearexpiry" name="yearexpiry" min="21" maxlength="2" placeholder="YY" required><br>

                    <label for="cvv">CVV</label>
                    <input type="text" id="cvv" name="cvv" placeholder="352"><br><br>
                    <input type="submit" value="Pay £<%out.print(invoiceItem.getCost());%>" class="btn">
                    <%}else{%>
                    <h5>Payment for this invoice is paid in full.</h4>
                    <%}%>
                </div>
            </form>
            <% 
            } catch (Exception e) {
            } %>
            </div>
          </div>
        </main>
    </div>
</div>
</body>
</html>