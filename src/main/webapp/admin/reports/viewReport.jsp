<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Report View</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../../res/components/titlebar.jsp" %>
            <main>
                <% out.print(request.getAttribute("generatedReport")); %>
            </main>
        </div>
    </div>
</body>
</html>
