--This script initialises the database, it starts by dropping the database, and then recreating the tables,
--it then initialises the basic users and their user details.


--Drop tables and constraints, DERBYDB doesnt support if exists so this will fail if you don't already have the tables

--the below drops where generated from the below commented line, if we change foreign keys we can rerun it to generate this:
--SELECT  'ALTER TABLE '||S.SCHEMANAME||'.'||T.TABLENAME||' DROP CONSTRAINT '||C.CONSTRAINTNAME||';'  FROM      SYS.SYSCONSTRAINTS C,      SYS.SYSSCHEMAS S,      SYS.SYSTABLES T  WHERE      C.SCHEMAID = S.SCHEMAID  AND      C.TABLEID = T.TABLEID  AND  S.SCHEMANAME = 'APP'  UNION  SELECT 'DROP TABLE ' || schemaname ||'.' || tablename || ';'  FROM SYS.SYSTABLES  INNER JOIN SYS.SYSSCHEMAS ON SYS.SYSTABLES.SCHEMAID = SYS.SYSSCHEMAS.SCHEMAID  where schemaname='APP';
ALTER TABLE PRESCRIPTIONS DROP CONSTRAINT FK_PRESCRIPTIONS_EMPLOYEE_ID;
ALTER TABLE PRESCRIPTIONS DROP CONSTRAINT FK_PRESCRIPTIONS_PATIENT_ID;
ALTER TABLE PRESCRIPTIONS DROP CONSTRAINT FK_PRESCRIPTIONS_APPOINTMENT_ID;
ALTER TABLE APPOINTMENTS DROP CONSTRAINT FK_APPOINTMENTS_EMPLOYEE_ID;
ALTER TABLE APPOINTMENTS DROP CONSTRAINT FK_APPOINTMENTS_PATIENT_ID;
ALTER TABLE INVOICE DROP CONSTRAINT FK_INVOICE_APPOINTMENT_ID;
ALTER TABLE INVOICE DROP CONSTRAINT FK_INVOICE_EMPLOYEE_ID;
ALTER TABLE INVOICE DROP CONSTRAINT FK_INVOICE_PATIENT_ID;
ALTER TABLE INVOICEITEM DROP CONSTRAINT FK_INVOICEITEM_INVOICE_ID;
ALTER TABLE USERDETAILS DROP CONSTRAINT FK_USERDETAILS_USER_ID;
DROP TABLE PRESCRIPTIONS;
DROP TABLE APPOINTMENTS;
DROP TABLE INVOICE;
DROP TABLE INVOICEITEM;
DROP TABLE SYSTEMUSER;
DROP TABLE USERDETAILS;


create table systemUser(
                           id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
                           username varchar(50) NOT NULL unique,
                           password varchar(255) NOT NULL,
                           active boolean NOT NULL DEFAULT false,
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
    appointmentStatus varchar(255) NOT NULL default 'PENDING',
    constraint fk_appointments_employee_id foreign key(employeeId) references userDetails(id),
    constraint fk_appointments_patient_id foreign key(patientId) references userDetails(id)
);

create table invoice(
                        id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
                        invoiceDate DATE NOT NULL default CURRENT_DATE,
                        invoiceTime TIME NOT NULL default CURRENT_TIME,
                        invoiceStatus varchar(16) NOT NULL DEFAULT 'UNPAID',
                        statusChangeDate Date NOT NULL default CURRENT_DATE,
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

create table systemSetting(
    settingKey varchar(100) not null primary key,
    value varchar(100) not null
);

create table prescriptions
(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    employeeId INTEGER NOT NULL,
    patientId INTEGER NOT NULL,
    prescriptionDetails  LONG VARCHAR NOT NULL,
    appointmentId INTEGER NOT NULL,
    issueDate DATE NOT NULL,

    constraint fk_prescriptions_employee_id foreign key(employeeId) references userDetails(id),
    constraint fk_prescriptions_patient_id foreign key(patientId) references userDetails(id),
    constraint fk_prescriptions_appointment_id foreign key(appointmentId) references appointments(id)
);

--The order of these are important they link to the above records

insert into systemUser(username, password, usergroup, active) values('admin', 'admin', 'ADMIN', true);
insert into systemUser(username, password, usergroup, active) values('receptionist', 'receptionist', 'RECEPTIONIST', true);
insert into systemUser(username, password, usergroup) values('doctor', 'doctor', 'DOCTOR');
insert into systemUser(username, password, usergroup) values('nurse', 'nurse', 'NURSE');
insert into systemUser(username, password, usergroup, active) values('patient', 'patient', 'PATIENT_NHS', true);
insert into systemUser(username, password, usergroup, active) values('patient2', 'patient2', 'PATIENT_PRIVATE', true);

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob)
values (1,'Tom', 'Thatcher', '64 East Street', '', '', 'Bristol', 'BS108TY', DATE('1982-05-13'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob)
values (2,'Casandra', 'Smith', '18 Richards Street', '', '', 'Bristol', 'BS078TP', DATE('1955-11-29'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob)
values (3,'Jake', 'Docotor', '52 John Road', '', '', 'Bristol', 'BS098TP', DATE('1980-12-20'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob)
values (4,'Sandra', 'Johnson', '64 Albert Street', '', '', 'Bristol', 'BS098TP', DATE('1982-05-13'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob)
values (5,'Jim', 'Smith', '12 Oak Road', '', '', 'Bristol', 'BS215TP', DATE('1995-02-01'));

insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob)
values (6,'Jane', 'Jones', '93 Snowflake Road', '', '', 'Bristol', 'BS45TP', DATE('2001-07-11'));

--create 2 days of appointments

insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID)
VALUES ('2020-11-30', '14:00:00', 3, 1);
insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID)
VALUES ('2020-11-30', '14:15:00', 3, 1);
insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID)
VALUES ('2020-11-30', '14:30:00', 3, 1);
insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID)
VALUES ('2020-11-30', '14:45:00', 3, 1);

insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID)
VALUES ('2020-12-01', '14:00:00', 3, 1);
insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID)
VALUES ('2020-12-01', '14:15:00', 3, 1);
insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID)
VALUES ('2020-12-01', '14:30:00', 3, 1);
insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID, appointmentStatus)
VALUES ('2020-12-01', '14:45:00', 3, 1, 'CANCELED');

--create an invoice for each appointment,
insert into INVOICE(INVOICEDATE, INVOICETIME, EMPLOYEEID,PATIENTID,INVOICESTATUS, PRIVATEPATIENT, APPOINTMENTID, STATUSCHANGEDATE)
VALUES ('2020-11-30', '14:15:00', 3, 1, 'OVERDUE', false, 1, '2020-11-01');
insert into INVOICE(INVOICEDATE, INVOICETIME, EMPLOYEEID,PATIENTID,INVOICESTATUS, PRIVATEPATIENT, APPOINTMENTID, STATUSCHANGEDATE)
VALUES ('2020-11-30', '14:15:00', 3, 1, 'OVERDUE', false, 2, '2020-11-01');
insert into INVOICE(INVOICEDATE, INVOICETIME, EMPLOYEEID,PATIENTID,INVOICESTATUS, PRIVATEPATIENT, APPOINTMENTID, STATUSCHANGEDATE)
VALUES ('2020-11-30', '14:30:00', 3, 1, 'PAID', false, 3, '2020-12-01');
insert into INVOICE(INVOICEDATE, INVOICETIME, EMPLOYEEID,PATIENTID,INVOICESTATUS, PRIVATEPATIENT, APPOINTMENTID, STATUSCHANGEDATE)
VALUES ('2020-11-30', '14:45:00', 3, 1, 'UNPAID', false, 4, '2020-11-01');

insert into INVOICE(INVOICEDATE, INVOICETIME, EMPLOYEEID,PATIENTID,INVOICESTATUS, PRIVATEPATIENT, APPOINTMENTID, STATUSCHANGEDATE)
VALUES ('2020-12-01', '14:00:00', 3, 1, 'PAID', false, 5, '2020-12-01');
insert into INVOICE(INVOICEDATE, INVOICETIME, EMPLOYEEID,PATIENTID,INVOICESTATUS, PRIVATEPATIENT, APPOINTMENTID, STATUSCHANGEDATE)
VALUES ('2020-12-01', '14:15:00', 3, 1, 'PAID', false, 6, '2020-12-01');
insert into INVOICE(INVOICEDATE, INVOICETIME, EMPLOYEEID,PATIENTID,INVOICESTATUS, PRIVATEPATIENT, APPOINTMENTID, STATUSCHANGEDATE)
VALUES ('2020-12-01', '14:30:00', 3, 1, 'UNPAID', false, 7, '2020-12-01');

--create invoice items
insert into INVOICEITEM(INVOICEID, ITEMCOST, QUANTITY, DESCRIPTION)
VALUES (1, 100.00, 1, 'Appointment Cost');
insert into INVOICEITEM(INVOICEID, ITEMCOST, QUANTITY, DESCRIPTION)
VALUES (2, 100.00, 1, 'Appointment Cost');
insert into INVOICEITEM(INVOICEID, ITEMCOST, QUANTITY, DESCRIPTION)
VALUES (3, 100.00, 1, 'Appointment Cost');
insert into INVOICEITEM(INVOICEID, ITEMCOST, QUANTITY, DESCRIPTION)
VALUES (4, 100.00, 1, 'Appointment Cost');

insert into INVOICEITEM(INVOICEID, ITEMCOST, QUANTITY, DESCRIPTION)
VALUES (5, 100.00, 1, 'Appointment Cost');
insert into INVOICEITEM(INVOICEID, ITEMCOST, QUANTITY, DESCRIPTION)
VALUES (6, 100.00, 1, 'Appointment Cost');
insert into INVOICEITEM(INVOICEID, ITEMCOST, QUANTITY, DESCRIPTION)
VALUES (7, 100.00, 1, 'Appointment Cost');

-- initialise system settings
insert into systemSetting(settingKey, value) values ('baseConsultationFeeDoctor', '150');
insert into systemSetting(settingKey, value) values ('baseConsultationFeeNurse', '100');
insert into systemSetting(settingKey, value) values ('consultationSlotTime', '10');
