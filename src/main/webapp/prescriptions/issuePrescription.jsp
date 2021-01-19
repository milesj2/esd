<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.AppointmentPlaceHolder" %>
<%@ page import="com.esd.controller.pagecontrollers.appointments.AppointmentBookingController" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.model.service.UserDetailsService" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>
<%@ page import="com.esd.model.data.persisted.ThirdParty" %>
<%@ page import="com.esd.model.data.PrescriptionRepeat" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.joda.time.LocalDate" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% SystemUser currentUser = (SystemUser) (session.getAttribute("currentSessionUser"));%>
<html>
<head>
    <title>Prescription issue</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <h1>Issue Prescription</h1>
            <form method="POST" action="${__SELF}">
                <% if(request.getAttribute("redirect") != null){ %>
                    <input type="hidden" name="redirect" value="<%=request.getParameter("redirect")%>">
                <% } %>
                <input type="hidden" name="selectedAppointmentId" value="<%=request.getParameter("selectedAppointmentId")%>">

               prescription Details</br>
                <textarea name="details" rows="20" cols="50"></textarea></br>
                Issue Date:
                <input type="date" name="issueDate" value="<%=new LocalDate()%>">
                Repeat:
                <select name="repeat">
                    <% for(PrescriptionRepeat repeat : PrescriptionRepeat.values()){ %>
                        <option value="<%=repeat.name()%>"><%=repeat.name()%></option>
                    <% } %>
                </select></br>
                Repeat until: <input type="date" name="repeatUntil" value="<%=new LocalDate()%>"></br>
                <input type="submit" name="issue" value="Issue"/>

                <% if(request.getAttribute("redirect") != null){ %>
                    <input type="submit" name="cancel" value="Cancel"/>
                <% } %>
            </form>
        </main>
    </div>
</div>
</body>
</html>