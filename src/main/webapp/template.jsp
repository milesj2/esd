<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Template</title>
    <link rel="stylesheet" href="res/css/master.css">
    <script src="res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="res/components/titlebar.jsp" %>
            <main>
            </main>
        </div>
    </div>
</body>
</html>
