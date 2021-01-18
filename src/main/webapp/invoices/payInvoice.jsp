<%@ page import="com.esd.model.data.persisted.Appointment" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.data.InvoiceStatus" %>
<%@ page import="com.esd.model.data.persisted.InvoiceItem" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>
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
            <script>
            function cc_format(value) {
              var v = value.replace(/\s+/g, '').replace(/[^0-9]/gi, '')
              var matches = v.match(/\d{4,16}/g);
              var match = matches && matches[0] || ''
              var parts = []
              for (i=0, len=match.length; i<len; i+=4) {
                parts.push(match.substring(i, i+4))
              }
              if (parts.length) {
                return parts.join(' ')
              } else {
                return value
              }
            }
            
            onload = function() {
              document.getElementById('cardnumber').oninput = function() {
                this.value = cc_format(this.value)
              }
            }
            
            function checkDigit(event) {
                var code = (event.which) ? event.which : event.keyCode;

                if ((code < 48 || code > 57) && (code > 31)) {
                    return false;
                }
                return true;
            }
            </script>
                
            <% try {
                Invoice invoice = (Invoice)request.getAttribute("invoice");
                SystemUser patient = (SystemUser)(request.getAttribute("patient"));
                SystemUser employee = (SystemUser)(request.getAttribute("employee"));
            %>
            
                <div class="invoice-info">
                    <h3>Invoice Details For Appointment No. <%out.print(invoice.getAppointmentId());%></h3>
                    <table border="1" cellpadding="5" class="search_table invoice">
                        <tr>
                            <th>Patient's Name </th>
                            <td><%= patient.getUserDetails().getFirstName() %>
                                <%=  patient.getUserDetails().getLastName() %></td>
                        </tr>
                        <tr>
                            <th>Full Address </th>
                            <td><%= patient.getUserDetails().getAddressLine1() %>
                                <%= patient.getUserDetails().getAddressLine2() %>
                                <%= patient.getUserDetails().getAddressLine3() %><br>
                                <%= patient.getUserDetails().getTown() %>, 
                                <%= patient.getUserDetails().getPostCode() %></td>
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
                            <td>£<%=(Double)request.getAttribute("totalDue")%></td>
                        </tr>
                    </table>
                </div>
                        
                <form method="post" action="${pageContext.request.contextPath}/invoices/pay?id=<%=invoice.getId()%>&uid=<%=invoice.getPatientId()%>&eid=<%=invoice.getEmployeeId()%>">
                <div class="invoice-pay">
                    <h3>Payment Details</h3>
                    <% if (invoice.getInvoiceStatus() != InvoiceStatus.PAID){%>
                    <label style="padding:5px;" for="fname">Accepted Cards</label>
                    <div class="icon-container" style="padding:5px;">
                      <i class="fa fa-cc-visa fa-2x" style="color:navy;"></i>
                      <i class="fa fa-cc-amex fa-2x" style="color:blue;"></i>
                      <i class="fa fa-cc-mastercard fa-2x" style="color:red;"></i>
                      <i class="fa fa-cc-discover fa-2x" style="color:orange;"></i>
                    </div><br>
                    <input type="text" name=<%=DaoConsts.ID%> value="<%out.print(invoice.getId());%>" size="10" hidden />
                    
                    <div style="padding:5px;"><label for="cardname">Name on Card*</label>
                    <input type="text" id="cardname" name="cardname" placeholder="John More Doe" required></div>

                    <div style="padding:5px;"><label for="cardnumber">Card number*</label>
                    <input size="16" value="" id="cardnumber" name="cardnumber" placeholder="1111 2222 3333 4444" onkeypress="return checkDigit(event)" required></div>

                    <div style="padding:5px;"><label for="expiry">Expiry Date* (MM/YY)</label>
                    <input type="number" id="monthexpiry" name="monthexpiry" min="01" max="12" maxlength="2" size="2" placeholder="MM" required><span>/</span>
                    <input type="number" id="yearexpiry" name="yearexpiry" min="21" max="30" maxlength="2" size="2" placeholder="YY" required></div>

                    <div style="padding:5px;"><label for="cvv">CVV*</label>
                        <input type="text" id="cvv" name="cvv" placeholder="352" size="3" maxlength="3" pattern="[0-9]{3}" required></div><br>
                    <input type="submit" value="Pay £<%=(Double)request.getAttribute("totalDue")%>" class="btn">
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