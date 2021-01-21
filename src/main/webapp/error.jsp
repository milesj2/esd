<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><%= request.getAttribute("title") %></title>
</head>
<body>
<nav><a href="<%= request.getAttribute("homeLink") %>"><%= request.getAttribute("homeLinkText")%></a></nav>
<h1><%= request.getAttribute("contentTitle") %></h1>
<p><%= request.getAttribute("contentBody") %></p>
</body>
</html>
