<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>
<%@ page import="com.esd.model.data.persisted.Prescription" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>

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
            <h1>Prescription</h1>
            <%
                UserDetails issuedToDetails = ((UserDetails)request.getAttribute("issuedToDetails"));
                UserDetails issuedByDetails = ((UserDetails)request.getAttribute("issuedByDetails"));
            %>
            Issued to: <%=issuedToDetails.getFirstName() + issuedToDetails.getLastName()%></br>
            <%=issuedToDetails.getFullAddress()%>
            Issued by:<%=issuedByDetails.getFirstName() + issuedByDetails.getLastName()%></br>
            Prescribed:</br><%=((Prescription)request.getAttribute("prescription")).getPrescriptionDetails()%></br>

            Repeated prescriptions:</br>
            <%
                List<Prescription> prescriptions = (List<Prescription>) request.getAttribute("childPrescriptions");
                for (Prescription prescription : prescriptions) {
            %>
            Issue Date: <%=prescription.getIssueDate()%>
            <a href="<%=UrlUtils.absoluteUrl(request, "prescriptions/view?selectedPrescriptionId="+prescription.getId())%>">
                View Prescription
            </a>
            <% } %>
            <a href="#">Download prescription</a>
        </main>
    </div>
</div>
</body>
</html>