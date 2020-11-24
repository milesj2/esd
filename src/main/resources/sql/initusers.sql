create table tblUser(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username varchar(255) NOT NULL unique,
    password varchar(255) NOT NULL,
    usergroup varchar(255) NOT NULL
);

insert into tblUser(username, password, usergroup) values('patient', 'patient', 'PATIENT');
insert into tblUser(username, password, usergroup) values('admin', 'admin', 'ADMIN');
insert into tblUser(username, password, usergroup) values('doctor', 'doctor', 'DOCTOR');
insert into tblUser(username, password, usergroup) values('nurse', 'nurse', 'NURSE');
insert into tblUser(username, password, usergroup) values('receptionist', 'receptionist', 'RECEPTIONIST');

