<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% SystemUser currentSystemUser = (SystemUser)(session.getAttribute("currentSessionUser"));%>
<% SystemUser user = (SystemUser) request.getAttribute("user");
   String deleteHash = (String) request.getAttribute("deleteHash");
   String contextPath = request.getContextPath();
 %>
<html>
<head>
    <title>Template</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
				<h1>WARNING!</h1>
				<h2>Are you sure you want to delete user?</h2>
				<h3>This action <b>cannot</b> be undone.</h3>
				<div>
                    <form method="post" action="delete" class="input_form">
                        <input hidden name="userID" value="<% out.print(user.getId()); %>">
                        <input hidden name="deleteHash" value="<% out.print(deleteHash); %>">
                        <input type="submit" value="Delete">
                    </form>
					<input type="button" onclick="window.location = '<% out.print(contextPath); %>/users/manage';" value="Cancel">
				</div>
            </main>
        </div>
    </div>
</body>
</html>
