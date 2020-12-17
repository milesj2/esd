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
