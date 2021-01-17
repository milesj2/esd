<%@ page import="com.esd.model.data.persisted.ThirdParty" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Third Party Management</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
            <h2>Manage Third Party</h2>
            <p style="color:Red">
                <%  String errMsg = request.getParameter("errMsg");
                    if (errMsg != null){
                        out.print(errMsg);
                    } %>
            </p>
            <a href='${pageContext.request.contextPath}/thirdPartyManagement/add'><button>Add Third Party</button></a> <hr>
            <table class="search_table">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Address Line 1</th>
                    <th>Address Line 2</th>
                    <th>Address Line 3</th>
                    <th>Town</th>
                    <th>Postcode</th>
                    <th>Type</th>
                    <th>Manage</th>
                </tr>
                <%

                    ArrayList<ThirdParty> thirdParties = (ArrayList<ThirdParty>) (request.getAttribute("thirdParties"));

                    for (ThirdParty thirdParty:thirdParties){
                        out.print("<tr>");
                        out.print(String.format("<td>%d</td>", thirdParty.getId()));
                        out.print(String.format("<td>%s</td>", thirdParty.getName()));
                        out.print(String.format("<td>%s</td>", thirdParty.getAddressLine1()));
                        out.print(String.format("<td>%s</td>", thirdParty.getAddressLine2()));
                        out.print(String.format("<td>%s</td>", thirdParty.getAddressLine3()));
                        out.print(String.format("<td>%s</td>", thirdParty.getTown()));
                        out.print(String.format("<td>%s</td>", thirdParty.getPostCode()));
                        out.print(String.format("<td>%s</td>", thirdParty.getType()));
                        out.print(String.format("<td><a href='edit?id=%d'>Edit</a></td>", thirdParty.getId()));
                        out.print("</tr>");
                    }
                %>
            </table>
            </main>
        </div>
    </div>        
</body>
</html>

