<div class="title_bar">
    <% if(request.getAttribute("pageTitle") != null){ %>
    <h1><% out.print(request.getAttribute("pageTitle"));%></h1>
    <%}%>
</div>