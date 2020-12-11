<%-- 
    Document   : signup
    Created on : 02-Dec-2020, 22:39:21
    Author     : angela
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import = "java.util.Date" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Register</title>
    <%
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String max_date = sdf.format(new Date());
    %>
</head>
    <body class="signup-page container">
    <!-- Validation to make sure password and confirm password match before pressing register button -->
        <script type="text/javascript"> 
        var check = function() {
          if (document.getElementById('password').value ===
            document.getElementById('confirm_password').value) {
            document.getElementById('message').style.color = 'green';
            document.getElementById('message').innerHTML = '';
            document.getElementById('submit').disabled = false;
          } else {
            document.getElementById('message').style.color = 'red';
            document.getElementById('message').innerHTML = 'Password not matching';
            document.getElementById('submit').disabled = true;
          }
        }
        </script>
    
        <img src="images/logo.png" class="loginLogo" alt="" />
        <h1>Register</h1>
        <p>Please fill in this form to create an account.</p>
        <hr>
        <form action="RegisterUser" name="register" method="post">

            <h4>Personal Details</h4>

            <label for="firstName">First Name: *</label>
            <input type="text" placeholder="Enter your First Name" name="firstName" id="firstName" required><br>

            <label for="lastName">Last Name: *</label>
            <input type="text" placeholder="Enter your Last Name" name="lastName" id="lastName" required><br>

            <label for="dob">Date of Birth: *</label>
            <input id="dob" name="dob" type="date" min="1900-01-01" max="<%=max_date%>" required><br>

            <label for="addressLine1">Street Address: *</label>
            <input type="text" placeholder="Enter your first line of address" name="addressLine1" id="addressLine1" required><br>

            <label for="addressLine2">Street Address Line 2:</label>
            <input type="text" placeholder="Enter your second line of address" name="addressLine2" id="addressLine2"><br>

            <label for="addressLine3">Street Address Line 3:</label>
            <input type="text" placeholder="Enter your third line of address" name="addressLine3" id="addressLine3"><br>

            <label for="town">Town: *</label>
            <input type="text" placeholder="Enter your Town" name="town" id="town" required><br>

            <label for="postCode">Postcode: *</label>
            <input type="text" placeholder="Enter your Postcode" name="postCode" id="postCode" required><br>
            
            <label for="userGroup">Patient Group:</label>
            <select id="userGroup" name="userGroup">
              <option value="NHS_PATIENT">NHS Patient</option>
              <option value="PRIVATE_PATIENT">Private Patient</option>
              <option value="DOCTOR">Doctor</option>
              <option value="NURSE">Nurse</option>
              <option value="RECEPTIONIST">Receptionist</option>
            </select><br><br>

            <h4>Login Details</h4>

            <label for="username">Username: *</label>
            <input type="text" placeholder="Enter your username" name="username" id="username" required><br>

            <label for="password">Password: *</label>
            <input type="password" onkeyup='check();' placeholder="Enter Password" name="password" id="password" required><br>

            <label for="confirm_password">Confirm Password: *</label>
            <input type="password" onkeyup='check();' placeholder="Confirm Password" name="confirm_password" id="confirm_password" required><br>
            <span id='message'></span>

            <hr>
            <p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>
            <button type="Submit" class="btn btn-register" value="Send" id="submit" disabled>Register</button>
            <% if ( request.getAttribute("notify") != null ) { %>
            <div class="registration_msg"><strong><%=request.getAttribute("notify")%></strong></div><br>
            <% } %>
            <p>Already have an account? <a href="index.jsp">Sign in</a>.</p>
        </form>        
    </body>
</html>