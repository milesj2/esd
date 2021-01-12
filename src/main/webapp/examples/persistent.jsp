<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Example page for persistent searching of form data</title>
</head>
<body>

        <form>
                <input type="submit" name="selectPatient" value="select patient" formaction="${__SELF}">
            <%  if (request.getAttribute("patientId") != null){ %>
                <input type="text" name="patientId" disabled value="<%= request.getAttribute("patientId") %>">
            <% } %>
            <input type="hidden" name="action" value="">
            <input type="text" name="dummyInput" value="<%= request.getAttribute("dummyInput") %>">
            <input type="submit" name="confirm" value="confirm" formaction="${__SELF}">
        </form>
</body>
</html>
