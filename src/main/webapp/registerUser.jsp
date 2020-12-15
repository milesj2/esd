<%@page import="com.esd.model.dao.DaoConsts"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import = "java.util.Date" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<%@ page import="com.esd.model.data.UserGroup" %>
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
        <form action="registerUser" name="register" method="post">

            <h4>Personal Details</h4>

            <label for="firstname">First Name: *</label>
            <input type="text" placeholder="Enter your First Name" name="<%=DaoConsts.USERDETAILS_FIRSTNAME%>" id="firstname" required><br>

            <label for="lastname">Last Name: *</label>
            <input type="text" placeholder="Enter your Last Name" name="<%=DaoConsts.USERDETAILS_LASTNAME%>" id="lastname" required><br>

            <label for="dob">Date of Birth: (YYYY-MM-DD) *</label>
            <input id="dob" name="<%=DaoConsts.USERDETAILS_DOB%>" type="date" min="1900-01-01" max="<%=max_date%>" required><br>

            <label for="addressline1">Street Address: *</label>
            <input type="text" placeholder="Enter your first line of address" name="<%=DaoConsts.USERDETAILS_ADDRESS1%>" id="addressline1" required><br>

            <label for="addressline2">Street Address Line 2:</label>
            <input type="text" placeholder="Enter your second line of address" name="<%=DaoConsts.USERDETAILS_ADDRESS2%>" id="addressline2"><br>

            <label for="addressline3">Street Address Line 3:</label>
            <input type="text" placeholder="Enter your third line of address" name="<%=DaoConsts.USERDETAILS_ADDRESS3%>" id="addressline3"><br>

            <label for="town">Town: *</label>
            <input type="text" placeholder="Enter your Town" name="<%=DaoConsts.USERDETAILS_TOWN%>" id="town" required><br>

            <label for="postcode">Postcode: *</label>
            <input type="text" placeholder="Enter your Postcode" name="<%=DaoConsts.USERDETAILS_POSTCODE%>" id="postcode" required><br>
            
            <label for="usergroup">Patient Group:</label>
            <select id="usergroup" name="<%=DaoConsts.SYSTEMUSER_USERGROUP%>">
              <option value="<%=UserGroup.NHS_PATIENT%>">NHS Patient</option>
              <option value="<%=UserGroup.PRIVATE_PATIENT%>">Private Patient</option>
              <option value="<%=UserGroup.DOCTOR%>">Doctor</option>
              <option value="<%=UserGroup.NURSE%>">Nurse</option>
              <option value="<%=UserGroup.RECEPTIONIST%>">Receptionist</option>
            </select><br><br>

            <h4>Login Details</h4>

            <label for="username">Username: *</label>
            <input type="text" placeholder="Enter your username" name="<%=DaoConsts.SYSTEMUSER_USERNAME%>" id="username" required><br>

            <label for="password">Password: *</label>
            <input type="password" onkeyup='check();' placeholder="Enter Password" name="<%=DaoConsts.SYSTEMUSER_PASSWORD%>" id="password" required><br>

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