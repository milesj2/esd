<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.controller.pagecontrollers.dashboard.DashboardWidget" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% SystemUser currentSystemUser = (SystemUser)(session.getAttribute("currentSessionUser"));%>

<%@ page import="com.esd.model.data.QuickNotification" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Dashboard</title>
    <link rel="stylesheet" href="res/css/master.css">
    <script src="res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="res/components/titlebar.jsp" %>
            <main>
                <h1>Dashboard</h1>
                Welcome, <% out.print(currentSystemUser.getUsername()); %>. Your privilege level is <% out.print(currentSystemUser.getUserGroup().name()); %>.
                <br>
                <div id="dashboard" class="widgets">
                    <% for (DashboardWidget widget : (List<DashboardWidget>)request.getAttribute("widgets")) { %>
                        <div class="quarter-widget">
                            <a href="<%=widget.getLink()%>">
                                <img src="<%=widget.getIcon()%>">
                                <h2><%=widget.getName()%></h2>
                            </a>
                        </div>
                    <% } %>
                </div>
                <div>
                    <h3>Quick Notifications</h3>
                    <% List<QuickNotification> quickNotifications = (List<QuickNotification>) request.getAttribute("quickNotifications"); %>
                    <% for (QuickNotification q: quickNotifications) { %>
                        <strong><%= q.getTitle() %></strong>
                        <% for (String line: q.getLines() ) { %>
                            <br>
                            <%= line %>
                        <% } %>
                        <br>
                    <% } %>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
