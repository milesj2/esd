let previous_sort_header;


function onDropDownClick(section) {

    var img = section.firstElementChild;
    if(img.src.includes("right")) {
        img.src = img.src.replace("chevron-right.png", "chevron-down.png");
    } else {
        img.src = img.src.replace("chevron-down.png", "chevron-right.png");
    }
    console.log(img.src);
    if (section.nextElementSibling == null){
        return;
    }
    var dropdownChild = section.nextElementSibling;
    // console.log(dropdownChild);
    dropdownChild.classList.toggle("show");
}

function filterTable(e){
    let value = e.value.toUpperCase();
    let table = e.parentElement.parentElement.parentElement;
    let rows = table.childNodes;
    let colIdx = e.parentElement.cellIndex;

    var i;
    for (i = 3; i < rows.length; i++){
        var row = rows[i];
        if (row.nodeName === "#text") {
            continue;
        }
        row.hidden = !row.children[colIdx].innerHTML.includes(value);
    }
}

function sortTable(e){
    // Modified: https://www.w3schools.com/howto/howto_js_sort_table.asp
    var rows, switching, i, x, y, shouldSwitch, img, sort, header;

    if (e.target.tag === "img"){
        header = e.currentTarget.parentElement;
    } else {
        header = e.currentTarget;
    }

    sort = header.getAttribute("sort");
    if (sort === "lock") {
        return;
    }
    if (sort === null) {
        if (previous_sort_header != null){
            previous_sort_header.innerHTML = header.innerHTML.split("<img")[0];
        }
        previous_sort_header = header;
        img = document.createElement("img")
        img.src = contextPath + "/res/icons/chevron-down.png";
        header.appendChild(img);
        header.setAttribute("sort", "down");
    } else if (sort === "up") {
        header.children[0].src = contextPath + "/res/icons/chevron-down.png";
        header.setAttribute("sort", "down");
    } else if (sort === "down") {
        header.children[0].src = contextPath + "/res/icons/chevron-up.png";
        header.setAttribute("sort", "up");
    }

    var cellIndex = header.cellIndex;
    var table = header.parentElement.parentElement.parentElement;

    switching = true;
    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            if (rows[i].getAttribute("sort") === "lock")
                continue;
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[cellIndex];
            y = rows[i + 1].getElementsByTagName("TD")[cellIndex];
            if (sort === "down"){
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            } else {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}


function addFuncToTableControl(){
    var i, j, searchTables;

    searchTables = document.getElementsByClassName("search_table");

    for (i = 0; i < searchTables.length; i++) {

        var searchTable = searchTables[i];
        var headers = searchTable.getElementsByTagName("th");
        for (j = 0; j < headers.length; j++) {
            var header = headers[j];
            header.onclick = function(e) {
                sortTable(e)
            };
        }
    }
}


function checkPasswords(){
    var err;

    let newPassword = document.getElementById("new_password");
    let confirmPassword = document.getElementById("confirm_password");

    if (confirmPassword.value === ""){
        return;
    }
    let passwordErrorContainer = document.getElementById("password_error_container");

    if (newPassword.value !== confirmPassword.value) {
        err = "Passwords do not match!";
    }
    else if (document.getElementById("password").value === "") {
        err = "Current password cannot be empty!";
    }
    else if (!isStrongPassword(newPassword.value)){
        err = "New password not strong enough!"
    }

    if (err !== undefined){
        passwordErrorContainer.classList.add("show");
        let passwordError = document.getElementById("password_error");
        passwordError.innerHTML = err;
    }  else {
        document.getElementById("password_submit").disabled = false;
        passwordErrorContainer.classList.remove("show");
    }
}

function isStrongPassword(password) {
    var regExp = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()]).{8,}/;
    return regExp.test(password);
}
