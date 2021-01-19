const weekday = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
const months = ["Jan", "Feb", "Mar","Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

function get(name){
    if(name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

function onAppointmentClick(e){
    window.location = getContextPath() + "/appointments/viewAppointment?id=" + e.currentTarget.id;
}

function viewDay(){
    let weekView = document.getElementById("week_schedule");
    let dayView = document.getElementById("day_schedule");

    weekView.style.display = "none";
    dayView.style.display = "block";
}

function viewWeek(){
    let weekView = document.getElementById("week_schedule");
    let dayView = document.getElementById("day_schedule");

    weekView.style.display = "block";
    dayView.style.display = "none";
}

function move_appointments(timeStart, timeStop, appointmentLength){
    const day_containers = document.getElementsByClassName("appointments_day_container");
    const time_container = document.getElementById("times");
    const height = time_container.offsetHeight;
    const hourHeight = Math.floor(height / (timeStop - timeStart + 1));
    const minuteHeight = Math.floor(hourHeight / (60 / appointmentLength));
    const offset = time_container.offsetTop;
    console.log(offset);
    console.log(hourHeight, timeStart, timeStop, height);

    var day, i, appointment;
    for (day = 0; day < day_containers.length; day++){
        for (i = 0; i < day_containers[day].childElementCount; i++) {
            appointment = day_containers[day].children[i];
            var time = appointment.getAttribute("time");
            if (time == null){
                continue;
            }
            var hour = time.split(":")[0];
            var minute = time.split(":")[1];
            // appointment.style.height = minuteHeight.toString();
            var hourPos = hourHeight * (hour - timeStart);
            var minutePos = minuteHeight/60 * minute;
            console.log(hourPos, minutePos);
            appointment.style.top = (offset + hourPos + minutePos).toString();
        }
    }
}

function generateDaysOfWeek(){
    let day_headers = document.getElementById("day_headers");
    let day = new Date(get("fromDate"));

    var i;
    for(i = day.getDay(); i < day.getDay() + 7; i++){
        var div = document.createElement("div");
        div.classList.add("day_header");
        var h3 = document.createElement("h3");
        console.log(day.getDay(), i, i % 7);
        h3.innerHTML = weekday[i % 7];

        var divider = document.createElement("div");
        divider.classList.add("vl");

        div.appendChild(h3);
        div.appendChild(divider);

        day_headers.appendChild(div);
    }
}

function generateTimes(timeStart, timeStop){
    var time;
    var timesContainer = document.getElementById("times");

    for (time = timeStart; time <= timeStop; time++) {
        var timeContainer = document.createElement("div");
        var span = document.createElement("span");
        if (time < 10){
            span.innerHTML = "0" + time + ":00";
        } else {
            span.innerHTML = time + ":00";
        }
        timeContainer.classList.add("hour_row");
        timeContainer.appendChild(span);
        timesContainer.appendChild(timeContainer);
    }
}

function generateAppointments(appointments){
    const day_containers = document.getElementsByClassName("appointments_day_container");

    let fromDate = new Date(get("fromDate"));

    var i;
    for (i = 0; i < appointments.length; i++){
        var appointment = appointments[i];
        var div = document.createElement("div");
        var h2 = document.createElement("h2");
        var h3 = document.createElement("h3");

        div.classList.add("appointment_container");
        div.setAttribute("id", appointment.id);
        div.setAttribute("time", appointment.time);
        div.onclick = function(e) {
            onAppointmentClick(e)
        };
        h2.innerHTML = appointment.title;
        // h3.innerHTML = appointment.slots;

        div.appendChild(h2);
        div.appendChild(h3);

        var timeDiff = Math.abs(new Date(appointment.date).getTime() - fromDate.getTime());
        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));

        day_containers[diffDays].appendChild(div);
    }
}

function generateDayScheduleDayContainers(){
    let dayScheduleContainer = document.getElementById("day_schedule");
    let day = new Date(get("fromDate"));
    var i;
    for(i = day.getDay(); i < day.getDay() + 7; i++) {
        var div = document.createElement("div");
        div.classList.add("day_container");

        var headerDiv = document.createElement("div")
        headerDiv.classList.add("day_header_container");

        var h3 = document.createElement("h3");
        h3.innerHTML = weekday[i % 7];

        var vl = document.createElement("div");
        vl.classList.add("vl");

        var innerDiv = document.createElement("div");

        headerDiv.appendChild(h3);
        headerDiv.appendChild(vl);
        div.appendChild(headerDiv);
        div.appendChild(innerDiv);

        dayScheduleContainer.appendChild(div);
    }
}

function appendAppointmentToDaySchedule(appointment, date){
    let dayScheduleContainer = document.getElementById("day_schedule");
    let fromDate = new Date(get("fromDate"));
    var pos = date.getDay() - fromDate.getDay();

    dayScheduleContainer.children[pos + 1].children[1].appendChild(appointment);
}

function generateDayScheduleAppointments(){
    var i;
    for (i = 0; i < appointments.length; i++){
        var appointment = appointments[i];
        var div = document.createElement("div");
        div.classList.add("appointment_container")
        div.setAttribute("id", appointment.id);
        div.onclick = function(e) {
            onAppointmentClick(e)
        };

        var title = document.createElement("h2");
        var time = document.createElement("h2");

        time.innerHTML = appointment.time.split(":").slice(0,2).join(":");
        title.innerHTML = appointment.title;
        div.appendChild(time);
        div.appendChild(title);

        appendAppointmentToDaySchedule(div, new Date(appointment.date));
    }
}

function initialise_day_schedule(){
    generateDayScheduleDayContainers();
    generateDayScheduleAppointments();
}

function initialise_schedule(){
    generateTimes(8, 18);
    generateDaysOfWeek();
    generateAppointments(appointments);
    move_appointments(8, 18, 10);
    viewDay();

    let day = new Date(get("fromDate"));
    var date = months[day.getMonth()] + " " + day.getDate() + " " + day.getFullYear();
    document.getElementById("week_beginning").innerHTML = "WB - " + date
}