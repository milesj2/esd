<%@page import="com.esd.model.dao.DaoConsts"%>
<%@ page import="com.esd.model.data.persisted.ThirdParty" %>
<%@ page import="com.esd.model.data.UserGroup" %>
<%@ page import="com.esd.model.data.ThirdPartyType" %>
<%@ page import="java.util.Arrays" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ThirdParty thirdParty = (ThirdParty)(request.getAttribute("editThirdParty"));
    String[] thirdPartyTypes = Arrays.toString(ThirdPartyType.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Third Party</title>
<<<<<<< HEAD
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
    <div class="root_container">
        <%@ include file="../res/components/sidebar.jsp" %>
        <div class="main_container">
            <%@ include file="../res/components/titlebar.jsp" %>
            <main>
            <h2>Editing: <%= thirdParty.getName() %></h2>

            <button onclick="window.location = '${pageContext.request.contextPath}/thirdPartyManagement/manage';">Cancel</button>
            <form method="post" action="${pageContext.request.contextPath}/thirdPartyManagement/edit" id="thirdPartyEdit">
                <input type="submit" name="saveEdit" value="Save">
                <br>
                <input type="hidden" name="<%=DaoConsts.THIRDPARTY_ID%>" value="<%= thirdParty.getId() %>">
                <br>
                <label for="thirdPartyName">Name</label>
                <input id="thirdPartyName" name="<%=DaoConsts.THIRDPARTY_NAME%>" type="text" value="<%= thirdParty.getName() %>">
                <br>
                <label for="addressline1">Address Line 1</label>
                <input id="addressline1" name="<%=DaoConsts.THIRDPARTY_ADDRESS1%>" type="text" value="<%= thirdParty.getAddressLine1() %>">
                <br>
                <label for="addressline2">Address Line 2</label>
                <input id="addressline2" name="<%=DaoConsts.THIRDPARTY_ADDRESS2%>" type="text" value="<%= thirdParty.getAddressLine2() %>">
                <br>
                <label for="addressline3">Address Line 3</label>
                <input id="addressline3" name="<%=DaoConsts.THIRDPARTY_ADDRESS3%>" type="text" value="<%= thirdParty.getAddressLine3() %>">
                <br>
                <label for="town">Town</label>
                <input id="town" name="<%=DaoConsts.THIRDPARTY_TOWN%>" type="text" value="<%= thirdParty.getTown() %>">
                <br>
                <label for="postCode">Postcode</label>
                <input id="postCode" name="<%=DaoConsts.THIRDPARTY_POSTCODE%>" type="text" value="<%= thirdParty.getPostCode() %>">
                <br>
                <label for="thirdPartyType">Type</label>
                <select name="<%=DaoConsts.THIRDPARTY_TYPE%>" id="thirdPartyType" form="thirdPartyEdit">
                    <%
                        for(String type:thirdPartyTypes){
                            out.print("<option value='" + type + "' ");
                            if (type.equals(thirdParty.getType())){
                                out.print("selected='selected'");
                            }
                            out.print(">" + type + "</option>");
                        }
                    %>
                </select>
                <br>
                <label>Account Active</label>
                <br>
                <label class="switch">
                    <input name="active" type="checkbox" class="switch-input" <% if(thirdParty.isActive()) out.print("checked");%>>
                    <span class="switch-label" data-on="on" data-off="off"></span>
                    <span class="switch-handle"></span>
                </label>
            </form>
            </main>
        </div>       
    </div>                
=======
</head>
<body>
    <img src="../res/images/logo.png" class="loginLogo" alt="" />
    <h1>Editing Third Party</h1>
    <h2>Name: <%= thirdParty.getName() %></h2>
    
    <button onclick="window.location = '${pageContext.request.contextPath}/thirdPartyManagement/manage';">Cancel</button>
    <form method="post" action="${pageContext.request.contextPath}/thirdPartyManagement/edit" id="thirdPartyEdit">
        <input type="submit" name="saveEdit" value="Save">
        <br>
        <input type="hidden" name="<%=DaoConsts.THIRDPARTY_ID%>" value="<%= thirdParty.getId() %>">
        <br>
        <label for="thirdPartyName">Name</label>
        <input id="thirdPartyName" name="<%=DaoConsts.THIRDPARTY_NAME%>" type="text" value="<%= thirdParty.getName() %>">
        <br>
        <label for="addressline1">Address Line 1</label>
        <input id="addressline1" name="<%=DaoConsts.THIRDPARTY_ADDRESS1%>" type="text" value="<%= thirdParty.getAddressLine1() %>">
        <br>
        <label for="addressline2">Address Line 2</label>
        <input id="addressline2" name="<%=DaoConsts.THIRDPARTY_ADDRESS2%>" type="text" value="<%= thirdParty.getAddressLine2() %>">
        <br>
        <label for="addressline3">Address Line 3</label>
        <input id="addressline3" name="<%=DaoConsts.THIRDPARTY_ADDRESS3%>" type="text" value="<%= thirdParty.getAddressLine3() %>">
        <br>
        <label for="town">Town</label>
        <input id="town" name="<%=DaoConsts.THIRDPARTY_TOWN%>" type="text" value="<%= thirdParty.getTown() %>">
        <br>
        <label for="postCode">Postcode</label>
        <input id="postCode" name="<%=DaoConsts.THIRDPARTY_POSTCODE%>" type="text" value="<%= thirdParty.getPostCode() %>">
        <br>
        <label for="thirdPartyType">Type</label>
        <select name="<%=DaoConsts.THIRDPARTY_TYPE%>" id="thirdPartyType" form="thirdPartyEdit">
            <%
                for(String type:thirdPartyTypes){
                    out.print("<option value='" + type + "' ");
                    if (type.equals(thirdParty.getType())){
                        out.print("selected='selected'");
                    }
                    out.print(">" + type + "</option>");
                }
            %>
        </select>
        <br>
        <label>Account Active</label>
        <br>
        <label class="switch">
            <input name="active" type="checkbox" class="switch-input" <% if(thirdParty.isActive()) out.print("checked");%>>
            <span class="switch-label" data-on="on" data-off="off"></span>
            <span class="switch-handle"></span>
        </label>
    </form>
>>>>>>> 588225e (51 - third party management)
</body>
</html>
