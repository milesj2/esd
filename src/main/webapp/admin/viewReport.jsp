<%--
  Created by IntelliJ IDEA.
  User: Jordan
  Date: 07/12/2020
  Time: 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Report View</title>
</head>
<body>
    <a href="<% out.print(request.getAttribute("backLink")); %>">BACK</a>
    <% out.print(request.getAttribute("generatedReport")); %>
</body>
</html>
