var weekday = new Array(7);
weekday[0] = "Sunday";
weekday[1] = "Monday";
weekday[2] = "Tuesday";
weekday[3] = "Wednesday";
weekday[4] = "Thursday";
weekday[5] = "Friday";
weekday[6] = "Saturday";

function get(name){
    if(name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
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
            appointment.style.height = minuteHeight.toString();
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

        div.classList.add("appointment");
        div.setAttribute("id", appointment.id);
        div.setAttribute("time", appointment.time);
        div.onclick = function(e) {
            onAppointmentClick(e)
        };
        h2.innerHTML = appointment.title;
        h3.innerHTML = appointment.slots;

        div.appendChild(h2);
        div.appendChild(h3);
        console.log(div);
        var timeDiff = Math.abs(new Date(appointment.date).getTime() - fromDate.getTime());
        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
        console.log(diffDays);
        day_containers[diffDays].appendChild(div);
    }
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

function onAppointmentClick(e){
    window.location = getContextPath() + "/appointments/viewAppointment?id=" + e.currentTarget.id;
}

function initialise_schedule(){
    generateTimes(8, 18);
    generateDaysOfWeek();
    generateAppointments(appointments);
    move_appointments(8, 18, 10);
}