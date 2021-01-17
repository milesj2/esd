<%@page import="com.esd.model.data.persisted.Appointment"%>
<%@page import="com.esd.model.data.UserGroup"%>
<%@page import="com.esd.model.data.InvoiceStatus"%>
<%@page import="com.esd.model.data.persisted.InvoiceItem"%>
<%@page import="com.esd.model.data.persisted.User"%>
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
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <h2>Invoice Payment</h2>
            <div class="payment-container">
              <a href='${pageContext.request.contextPath}/invoices/search'><button type="button">Back to Invoice Search</button></a>
              <%try {request.getParameter("message");} catch(Exception e) { } %>
              
              <form method="post" action="${pageContext.request.contextPath}/invoices/pay">
                <% try {
                    Invoice invoice = (Invoice)request.getAttribute("invoice");
                    InvoiceItem invoiceItem = (InvoiceItem)request.getAttribute("invoiceItem");
                    User user = (User)(request.getAttribute("user"));
                    User employee = (User)(request.getAttribute("employee"));
                    Appointment appointment = (Appointment)request.getAttribute("appointment");
                %>

                <div class="row">
                    <h3>Invoice Details</h3>
                    <table border="1" cellpadding="5" class="search_table">
                        <tr>
                            <td>Patient's Name </td>
                            <td><%= user.getUserDetails().getFirstName() %>
                                <%=  user.getUserDetails().getLastName() %></td>
                        </tr>
                        <tr>
                            <td>Full Address: </td>
                            <td><%= user.getUserDetails().getAddressLine1() %>
                                <%= user.getUserDetails().getAddressLine2() %>
                                <%= user.getUserDetails().getAddressLine3() %><br>
                                <%= user.getUserDetails().getTown() %><br>
                                <%= user.getUserDetails().getPostCode() %></td>
                        </tr>
                        <tr>
                            <td>Date</td>
                            <td><%out.print(invoice.getInvoiceDate());%></td>
                        </tr>
                        <tr>
                            <td>Time</td>
                            <td><%out.print(invoice.getInvoiceTime());%></td>
                        </tr>

                        <tr>
                            <td>Appointment With </td>
                        <% if (employee.getUserGroup() != UserGroup.DOCTOR){%>
                            <td>Nurse</td>
                        </tr>
                        <tr>
                            <td>Nurse's Name </td>
                            <td><%= employee.getUserDetails().getFirstName() %>
                                <%= employee.getUserDetails().getLastName() %></td>
                        </tr>
                        <%}else{%>
                            <td>Doctor</td>
                        </tr>
                        <tr>
                            <td>Doctor's Name </td>
                            <td><%= employee.getUserDetails().getFirstName() %>
                                <%= employee.getUserDetails().getLastName() %></td>
                        </tr>
                        <%}%>
                        <tr>
                            <td>Private Patient?</td>
                            <td><%out.print(invoice.isPrivatePatient());%></td>
                        </tr>
                        <tr>
                            <td>Appointment id</td>
                            <td><%out.print(invoice.getAppointmentId());%></td>
                        </tr>
                        <tr>
                            <td>Appointment Date</td>
                            <td><%out.print(appointment.getAppointmentDate());%></td>
                        </tr>
                        <tr>
                            <td>Appointment Time</td>
                            <td><%out.print(appointment.getAppointmentTime());%></td>
                        </tr>
                        <tr>
                            <td>Total Amount</td>
                            <td>£<%out.print(invoiceItem.getCost());%></td>
                        </tr>
                    </table>
                </div>
                <div>
                    <h3>Payment Details</h3>
                    <% if (invoice.getInvoiceStatus() != InvoiceStatus.PAID){%>
                    <label for="fname">Accepted Cards</label>
                    <div class="icon-container">
                      <i class="fa fa-cc-visa" style="color:navy;"></i>
                      <i class="fa fa-cc-amex" style="color:blue;"></i>
                      <i class="fa fa-cc-mastercard" style="color:red;"></i>
                      <i class="fa fa-cc-discover" style="color:orange;"></i>
                    </div><br>
                    
                    <label for="cardname">Name on Card*</label>
                    <input type="text" id="cardname" name="cardname" placeholder="John More Doe" required><br>

                    <label for="cardnumber">Card number*</label>
                    <input type="text" id="cardnumber" name="cardnumber" maxlength="16" placeholder="1111-2222-3333-4444" required><br>

                    <label for="expiry">Expiry Date* (MM/YY)</label>
                    <input type="number" id="monthexpiry" name="monthexpiry" min="01" max="12" maxlength="2" placeholder="MM" required><span>/</span>
                    <input type="number" id="yearexpiry" name="yearexpiry" min="21" maxlength="2" placeholder="YY" required><br>

                    <label for="cvv">CVV</label>
                    <input type="text" id="cvv" name="cvv" placeholder="352"><br><br>
                    <input type="submit" value="Pay Now" class="btn">
                    <% if ( request.getAttribute("message") != null ) { %>
                    <div class="msg"><strong><%=request.getAttribute("message")%></strong></div><br>
                    <% } %>
                    <%}else{%>
                    <h5>Payment for this invoice is paid in full.</h4>
                    <%}%>
                </div>
                <% 
                } catch (Exception e) {
                } %>
              </form>
            </div>
          </div>
        </main>
    </div>
</div>
</body>
</html>