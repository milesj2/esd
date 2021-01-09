<div class="sidebar_container">
    <a href="dashboard">
        <img class="logo" src="${pageContext.request.contextPath}/res/images/logo_small.png" alt="SmartWare Logo">
    </a>
    <nav>
        <a href="${pageContext.request.contextPath}/logout">
            <img src="${pageContext.request.contextPath}/res/icons/logout.png" alt="logout">
            Logout
        </a>
        <br>
        <a href="${pageContext.request.contextPath}/<% out.print(request.getAttribute("previousPage")); %>">
            <img src="${pageContext.request.contextPath}/res/icons/rotate-2.png" alt="back button image">
            Back
        </a>
        <h1>Navigation</h1>
        <ul>
            <li>
                <img class="dropdown_btn"
                     onclick="onDropDownClick(this)"
                     src="${pageContext.request.contextPath}/res/icons/chevron-right.png"
                     alt="users drop down list">
                <a href="#">Users</a>
                <ul class="dropdown_list">
                    <li>
                        <a href="#">Dropdown 1</a>
                    </li>
                    <li>
                        <a href="#">Dropdown 2</a>
                    </li>
                    <li>
                        <a href="#">Dropdown 3</a>
                    </li>
                </ul>
            </li>
            <li>
                <img id="reports"
                     onclick="onDropDownClick(this);"
                     src="${pageContext.request.contextPath}/res/icons/chevron-right.png"
                     alt="users drop down list">
                <a href="#">Reports</a>
            </li>
            <li>
                <img id="appointments"
                     onclick="onDropDownClick(this);"
                     src="${pageContext.request.contextPath}/res/icons/chevron-right.png"
                     alt="users drop down list">
                <a href="#">Appointments</a>
            </li>
        </ul>
    </nav>
</div>