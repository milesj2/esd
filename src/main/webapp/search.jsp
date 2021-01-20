<%@ page import="com.esd.model.dao.DaoConsts" %>
<%@ page import="com.esd.model.data.persisted.UserDetails" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.esd.controller.pagecontrollers.search.SearchColumn" %>
<%@ page import="com.esd.controller.pagecontrollers.search.searchrow.SearchRow" %>
<%@ page import="com.esd.controller.utils.UrlUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Search</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="res/components/titlebar.jsp" %>
        <main>
            <h2>Search page</h2>
            <h3>Enter your search terms</h3>
            <table border="1" cellpadding="5" class="search_table">
                <tr>
                    <% List<SearchColumn> columns = (List<SearchColumn>) request.getAttribute("columns");

                        for (SearchColumn column : columns) { %>
                        <th><%=column.getName()%></th>
                    <% } %>
                    <th sort="lock">Actions</th>
                </tr>

                <form method="post" action="${__SELF}">
                    <tr>
                        <% for (SearchColumn column : columns) { %>
                        <td><input type="<%=column.getType()%>" name=<%=column.getField()%> size="10"/></td>
                        <% } %>
                    </tr>
                    <tr>
                        <input type="submit" name="search" value="search"/>
                        <% if (request.getParameter("redirect") != null) {%>
                        <input type="submit" name="cancel" value="cancel"/>
                        <% } %>

                        <input type="hidden" name="type" value="search"/>
                    </tr>
                </form>
                <% try {
                    List<SearchRow> searchResults = (List<SearchRow>) request.getAttribute("table");
                    for (SearchRow row : searchResults) { %>
                <tr>
                    <% for (String column : row.getColumns()) { %>
                    <td><%=column%>
                    </td>
                    <% } %>
                    <td>
                        <% if (request.getParameter("redirect") != null) {%>
                        <form method="post" action="${__SELF}">
                            <input type="hidden" name="type" value="result"/>
                            <input type="hidden" name="selectedKey" value="<%=row.getId()%>"/>
                            <input type="submit" value="Select"/>
                        </form>
                        <%} else {
                            for(String link : row.getSearchActions().keySet()){ %>
                            <a href="<%=UrlUtils.absoluteUrl(request,link)%>"><%=row.getSearchActions().get(link)%></a>
                    <% }
                        }%>
                    </td>

                </tr>
                <%
                        }
                    } catch (Exception e) {
                    }
                %>
            </table>
        </main>
    </div>
    <script>
        var contextPath = "${pageContext.request.contextPath}"
        addFuncToTableControl();
    </script>
</body>
</html>

