<div class="sidebar_container">
    <a href="${pageContext.request.contextPath}/dashboard">
        <img class="logo" src="${pageContext.request.contextPath}/res/images/logo_small.png" alt="SmartWare Logo">
    </a>
    <nav>
        <a href="${pageContext.request.contextPath}/logout">
            <img src="${pageContext.request.contextPath}/res/icons/logout.png" alt="logout">
            Logout
        </a>
        <br>
        <a href="${pageContext.request.contextPath}/dashboard">
            <img src="${pageContext.request.contextPath}/res/icons/home.png" alt="home button image">
            Home
        </a>
        <br>
        <a href="${pageContext.request.contextPath}/<% out.print(request.getSession().getAttribute("previousPage")); %> ">
            <img src="${pageContext.request.contextPath}/res/icons/rotate-2.png" alt="back button image">
            Back
        </a>
        <h1>Navigation</h1>
        <ul>
            <li>
                <a style="padding-left: 22px" href="#">Account</a>
            </li>
            <li>
                <a style="padding-left: 22px" href="${pageContext.request.contextPath}/appointments/schedule">Appointments</a>
            </li>
            <li>
                <a class="dropdown_btn" onclick="onDropDownClick(this)" href="#">
                    <img src="${pageContext.request.contextPath}/res/icons/chevron-right.png" alt="systemUsers drop down list">
                    Example Drop Down
                </a>
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
        </ul>
    </nav>
</div>