<%@ page import="java.util.List" %>
<%@ page import="com.esd.model.data.persisted.SystemUser" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>
<%@ page import="com.esd.model.data.persisted.Prescription" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>
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
            <h1>Prescription</h1>
            <%
                Prescription prescription = (Prescription)request.getAttribute("prescription");
                UserDetails issuedToDetails = ((UserDetails)request.getAttribute("issuedToDetails"));
                UserDetails issuedByDetails = ((UserDetails)request.getAttribute("issuedByDetails"));
            %>
            Issued on: <%=prescription.getIssueDate()%></br>
            Issued to: <%=issuedToDetails.getFirstName() + issuedToDetails.getLastName()%></br>
            <%=issuedToDetails.getFullAddress()%></br></br>
            Issued by:<%=issuedByDetails.getFirstName() + issuedByDetails.getLastName()%></br></br>
            Prescribed:</br><%=((Prescription)request.getAttribute("prescription")).getPrescriptionDetails()%></br></br>

            <% List<Prescription> prescriptions = (List<Prescription>) request.getAttribute("childPrescriptions");
            if(prescription != null && !prescriptions.isEmpty()){ %>
                Repeated prescriptions:</br>
                <% for (Prescription p : prescriptions) { %>
                    Issue Date: <%=p.getIssueDate()%>
                    <a href="<%=UrlUtils.absoluteUrl(request, "prescriptions/view?selectedPrescriptionId="+p.getId())%>">
                        View Prescription
                    </a></br>
                <% } %>
            <% } %>

            <% if(prescription.getOriginatingPrescriptionId() != null && prescription.getOriginatingPrescriptionId() != 0){ %>
            Original Prescription: <a href="<%=UrlUtils.absoluteUrl(request, "prescriptions/view?selectedPrescriptionId="+prescription.getOriginatingPrescriptionId())%>">
                View Prescription
        </a>
            <% } %>
            </br>
            <%
                LocalDate issueDate = prescription.getIssueDate();
                if(issueDate.isAfter(new LocalDate())){
            %>
            <span>Prescription will be available for download on the issue date</span>

        <% } else { %>
            <a href="#">Download prescription</a>
        <% } %>

        </main>
    </div>
</div>
</body>
</html>