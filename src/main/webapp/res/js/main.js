/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function onDropDownClick(button) {
    if(button.src.includes("right")) {
        button.src.replace("res/icons/chevron-right.png", "res/icons/chevron-down.png");
    } else {
        button.src.replace("res/icons/chevron-down.png", "res/icons/chevron-right.png");
    }

    if (button.nextElementSibling == null){
        return;
    }
    var dropdownChild = button.nextElementSibling.nextElementSibling;
    dropdownChild.classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
    /*
    if (!event.target.matches('.dropdown_btn')) {
        var dropdowns = document.getElementsByClassName("dropdown_list");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
     */
}


function sortTable(e){
    // Modified: https://www.w3schools.com/howto/howto_js_sort_table.asp
    var rows, switching, i, x, y, shouldSwitch, img;

    var header = e.target;

    if (header.getAttribute("sort") === "lock"){
        return;
    }

    var cellIndex = header.cellIndex;
    var table = header.parentElement.parentElement.parentElement;

    var headers = table.getElementsByTagName("th");

    for(i = 0; i < headers.length; i++){
        header = headers[i];
        if (i === cellIndex){
            if (header.childElementCount > 0){
                img = header.children[0];
                if (img.src.includes("down")){
                    img.src = contextPath + "/res/icons/chevron-up.png";
                } else {
                    img.src = contextPath + "/res/icons/chevron-down.png";
                }
                break;
            }
            img = document.createElement("img")
            img.src = contextPath + "/res/icons/chevron-down.png";
            header.appendChild(img);
            continue;
        }
        header.innerHTML = header.innerHTML.split("<img")[0];
    }

    switching = true;
    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[cellIndex];
            y = rows[i + 1].getElementsByTagName("TD")[cellIndex];
            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}


function addFuncToTableControl(){

    var searchTables = document.getElementsByClassName("search_table");
    var i;
    var j;

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

