<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% SystemUser currentSystemUser = (SystemUser)(session.getAttribute("currentSessionUser"));%>
<% String userID = request.getAttribute("userID");
   String deleteHash = request.getAttribute("deleteHash");
   String contextPath = request.contextPath;
 %>
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
				<h1>WARNING!</h1>
				<h2>Are you sure you want to delete user?</h2>
				<h3>This action <b>cannot</b> be undone.</h3>
				<div>
					<input type="button" onClick="<% out.print(contextPath + "/users/delete?userID=" + userID + "&deleteHash=" + deleteHash); %>" value="Yes">
					<input type="button" onClick="<% out.print(contextPath); %>/users/manage" value="Cancel">
				</div>
            </main>
        </div>
    </div>
</body>
</html>
