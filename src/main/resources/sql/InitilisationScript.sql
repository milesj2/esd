--This script initilises the database, it starts by dropping the database, and then recreating the tables,
--it then initilises the basic users and there user details.


--Drop tables and constraints, DERBYDB doesnt support if exists so this will fail if you don't already have the tables

--the below drops where generated from the below commented line, if we change foreign keys we can rerun it to generate this:
--SELECT  'ALTER TABLE '||S.SCHEMANAME||'.'||T.TABLENAME||' DROP CONSTRAINT '||C.CONSTRAINTNAME||';'  FROM      SYS.SYSCONSTRAINTS C,      SYS.SYSSCHEMAS S,      SYS.SYSTABLES T  WHERE      C.SCHEMAID = S.SCHEMAID  AND      C.TABLEID = T.TABLEID  AND  S.SCHEMANAME = 'APP'  UNION  SELECT 'DROP TABLE ' || schemaname ||'.' || tablename || ';'  FROM SYS.SYSTABLES  INNER JOIN SYS.SYSSCHEMAS ON SYS.SYSTABLES.SCHEMAID = SYS.SYSSCHEMAS.SCHEMAID  where schemaname='APP';
ALTER TABLE  APP.APPOINTMENTS DROP CONSTRAINT FK_APPOINTMENTS_EMPLOYEE_ID;
ALTER TABLE APP.APPOINTMENTS DROP CONSTRAINT FK_APPOINTMENTS_PATIENT_ID;
ALTER TABLE APP.INVOICE DROP CONSTRAINT FK_INVOICE_APPOINTMENT_ID;
ALTER TABLE APP.INVOICE DROP CONSTRAINT FK_INVOICE_EMPLOYEE_ID;
ALTER TABLE APP.INVOICE DROP CONSTRAINT FK_INVOICE_PATIENT_ID;
ALTER TABLE APP.INVOICEITEM DROP CONSTRAINT FK_INVOICEITEM_INVOICE_ID;
ALTER TABLE APP.USERDETAILS DROP CONSTRAINT FK_USERDETAILS_USER_ID;
DROP TABLE APP.APPOINTMENTS;
DROP TABLE APP.INVOICE;
DROP TABLE APP.INVOICEITEM;
DROP TABLE APP.SYSTEMUSER;
DROP TABLE APP.USERDETAILS;


create table systemUser(
                           id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
                           username varchar(50) NOT NULL unique,
                           password varchar(255) NOT NULL,
                           userGroup varchar(16) NOT NULL
);

create table userDetails(
                            id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
                            userId Integer unique,
                            firstName varchar(50) NOT NULL,
                            lastName varchar(50) NOT NULL,
                            addressLine1 varchar(255) NOT NULL,
                            addressLine2 varchar(255) NOT NULL,
                            addressLine3 varchar(255) NOT NULL,
                            town varchar(255) NOT NULL,
                            postCode varchar(7) NOT NULL,
                            userGroup varchar(16) NOT NULL,
                            dob DATE NOT NULL,

                            constraint fk_userdetails_user_id foreign key(userId) references systemUser(id)
);

create table appointments
(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    appointmentDate DATE NOT NULL,
    appointmentTime TIME NOT NULL,
    slots INTEGER NOT NULL default 1,
    employeeId INTEGER NOT NULL,
    patientId INTEGER NOT NULL,

    constraint fk_appointments_employee_id foreign key(employeeId) references userDetails(id),
    constraint fk_appointments_patient_id foreign key(patientId) references userDetails(id)
);

create table invoice(
                        id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
                        invoiceDate DATE NOT NULL,
                        invoiceTime TIME NOT NULL,
                        invoiceStatus varchar(16),
                        employeeId integer not null,
                        patientId integer not null,
                        privatePatient BOOLEAN not null,
                        appointmentId integer not null,

                        constraint fk_invoice_employee_id foreign key(employeeId) references userDetails(id),
                        constraint fk_invoice_patient_id foreign key(patientId) references userDetails(id),
                        constraint fk_invoice_appointment_id foreign key(appointmentId) references appointments(id)
);

create table invoiceItem
(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    invoiceId integer not null,
    itemCost double not null,
    quantity integer not null,
    description varchar(100) not null,
    constraint fk_invoiceitem_invoice_id foreign key(invoiceId) references invoice(id)
);


/* create table prescription(
id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
);
 */

--The order of these are important they link to the above records

insert into systemUser(username, password, usergroup) values('admin', 'admin', 'ADMIN');
insert into systemUser(username, password, usergroup) values('receptionist', 'receptionist', 'RECEPTIONIST');
insert into systemUser(username, password, usergroup) values('doctor', 'doctor', 'DOCTOR');
insert into systemUser(username, password, usergroup) values('nurse', 'nurse', 'NURSE');
insert into systemUser(username, password, usergroup) values('patient', 'patient', 'PATIENT');

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, userGroup, dob)
values (1,'Tom', 'Thatcher', '64 East Street', '', '', 'Bristol', 'BS108TY', 'ADMIN', DATE('1982-05-13'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, userGroup, dob)
values (2,'casandra', 'smith', '18 Richards Street', '', '', 'Bristol', 'BS078TP', 'RECEPTIONIST', DATE('1955-11-29'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, userGroup, dob)
values (3,'jake', 'docotor', '52 john Road', '', '', 'Bristol', 'BS098TP', 'DOCTOR', DATE('1980-12-20'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, userGroup, dob)
values (4,'sandra', 'johnson', '64 Albert Stree', '', '', 'Bristol', 'BS098TP', 'NURSE', DATE('1982-05-13'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, userGroup, dob)
values (5,'jim', 'smith', '12 Oak Road', '', '', 'Bristol', 'BS215TP', 'PATIENT', DATE('1995-02-01'));
