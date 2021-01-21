<%@ page import="com.esd.model.data.WorkingHours" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% List<WorkingHours> workingHours = (List<WorkingHours>) request.getAttribute("workingHours"); %>
<% List<Integer> days = workingHours.get(0).getWorkingDays(); %>

<html>
<head>
    <title>Edit Working Hours</title>
    <link rel="stylesheet" href="../res/css/master.css">
    <script src="../res/js/main.js"></script>
</head>
<body>
<div class="root_container">
    <%@ include file="../res/components/sidebar.jsp" %>
    <div class="main_container">
        <%@ include file="../res/components/titlebar.jsp" %>
        <main>
            <form class="input_form" action="hours" method="post">
                <input name="id" hidden value="<%= request.getParameter("id") %>">
                <label for="startTime">Start Time</label>
                <input id="startTime" name="startTime" type="time" value="<%= workingHours.get(0).getStartTime() %>">
                <br>
                <label for="endTime">End Time</label>
                <input id="endTime" name="endTime" type="time" value="<%= workingHours.get(0).getEndTime() %>">
                <br>
                <br>
                <input id="monday" name="monday" type="checkbox" value="1" <% if(days.contains(1)) out.print("checked"); %> >
                <label for="monday">Monday</label>
                <br>
                <input id="tuesday" name="tuesday" type="checkbox" value="2" <% if(days.contains(2)) out.print("checked"); %>>
                <label for="tuesday">Tuesday</label>
                <br>
                <input id="wednesday" name="wednesday" type="checkbox" value="3" <% if(days.contains(3)) out.print("checked"); %>>
                <label for="wednesday">Wednesday</label>
                <br>
                <input id="thursday" name="thursday" type="checkbox" value="4" <% if(days.contains(4)) out.print("checked"); %>>
                <label for="thursday">Thursday</label>
                <br>
                <input id="friday" name="friday" type="checkbox" value="5" <% if(days.contains(5)) out.print("checked"); %>>
                <label for="friday">Friday</label>
                <br>
                <input id="saturday" name="saturday" type="checkbox" value="6" <% if(days.contains(6)) out.print("checked"); %>>
                <label for="saturday">Saturday</label>
                <br>
                <input id="sunday" name="sunday" type="checkbox" value="7" <% if(days.contains(7)) out.print("checked"); %>>
                <label for="sunday">Sunday</label>
                <br>
                <input type="submit" value="Submit">
            </form>
        </main>
    </div>
</div>
</body>
</html>
