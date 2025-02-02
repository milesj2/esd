--This script initialises the database, it starts by dropping the database, and then recreating the tables,
--it then initialises the basic systemUsers and their systemUser details.


--Drop tables and constraints, DERBYDB doesnt support if exists so this will fail if you don't already have the tables

--the below drops where generated from the below commented line, if we change foreign keys we can rerun it to generate this:
--SELECT  'ALTER TABLE '||S.SCHEMANAME||'.'||T.TABLENAME||' DROP CONSTRAINT '||C.CONSTRAINTNAME||';'  FROM      SYS.SYSCONSTRAINTS C,      SYS.SYSSCHEMAS S,      SYS.SYSTABLES T  WHERE      C.SCHEMAID = S.SCHEMAID  AND      C.TABLEID = T.TABLEID  AND  S.SCHEMANAME = 'APP'  UNION  SELECT 'DROP TABLE ' || schemaname ||'.' || tablename || ';'  FROM SYS.SYSTABLES  INNER JOIN SYS.SYSSCHEMAS ON SYS.SYSTABLES.SCHEMAID = SYS.SYSSCHEMAS.SCHEMAID  where schemaname='APP';
ALTER TABLE PRESCRIPTIONS DROP CONSTRAINT FK_PRESCRIPTIONS_EMPLOYEE_ID;
ALTER TABLE PRESCRIPTIONS DROP CONSTRAINT FK_PRESCRIPTIONS_PATIENT_ID;
ALTER TABLE PRESCRIPTIONS DROP CONSTRAINT FK_PRESCRIPTIONS_APPOINTMENT_ID;
ALTER TABLE WORKINGHOURSJT DROP CONSTRAINT FK_WORKINGHOURSJT_WORKINGHOURS_ID;
ALTER TABLE APPOINTMENTS DROP CONSTRAINT FK_APPOINTMENTS_THIRDPARTY_ID;
ALTER TABLE APPOINTMENTS DROP CONSTRAINT FK_APPOINTMENTS_EMPLOYEE_ID;
ALTER TABLE APPOINTMENTS DROP CONSTRAINT FK_APPOINTMENTS_PATIENT_ID;
ALTER TABLE INVOICE DROP CONSTRAINT FK_INVOICE_APPOINTMENT_ID;
ALTER TABLE INVOICE DROP CONSTRAINT FK_INVOICE_EMPLOYEE_ID;
ALTER TABLE INVOICE DROP CONSTRAINT FK_INVOICE_PATIENT_ID;
ALTER TABLE INVOICEITEM DROP CONSTRAINT FK_INVOICEITEM_INVOICE_ID;
ALTER TABLE USERDETAILS DROP CONSTRAINT FK_USERDETAILS_USER_ID;
DROP TABLE THIRDPARTY;
DROP TABLE PRESCRIPTIONS;
DROP TABLE WORKINGHOURS;
DROP TABLE WORKINGHOURSJT;
DROP TABLE APPOINTMENTS;
DROP TABLE INVOICE;
DROP TABLE INVOICEITEM;
DROP TABLE SYSTEMUSER;
DROP TABLE USERDETAILS;
DROP TABLE SYSTEMSETTING;

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
    postCode varchar(8) NOT NULL,
    dob DATE NOT NULL,
    constraint fk_userdetails_user_id foreign key(userId) references systemUser(id) ON DELETE CASCADE
);

create table thirdParty(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    thirdPartyName varchar(50) NOT NULL,
    addressLine1 varchar(255) NOT NULL,
    addressLine2 varchar(255) NOT NULL,
    addressLine3 varchar(255) NOT NULL,
    town varchar(255) NOT NULL,
    postCode varchar(7) NOT NULL,
    thirdPartyType varchar(20) NOT NULL,
    active boolean NOT NULL DEFAULT true
);

create table workingHours(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    workingDays varchar(255) NOT NULL,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL
);

create table workingHoursJT(
    employeeId INTEGER NOT NULL,
    workingHoursId INTEGER NOT NULL,
    constraint fk_workingHoursjt_workinghours_id foreign key(workingHoursId) references workingHours(id),
    constraint fk_workingHoursjt_employee_id foreign key(employeeId) references userDetails(id) ON DELETE CASCADE
);

create table appointments(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    appointmentDate DATE NOT NULL,
    appointmentTime TIME NOT NULL,
    slots INTEGER NOT NULL default 1,
    employeeId INTEGER NOT NULL,
    patientId INTEGER NOT NULL,
    appointmentStatus varchar(255) NOT NULL default 'PENDING',
    thirdPartyId INTEGER,
    notes LONG VARCHAR default 'n/a',
    appointmentReason LONG VARCHAR default '',
    constraint fk_appointments_thirdParty_id foreign key(thirdPartyId) references thirdParty(id) ON DELETE CASCADE,
    constraint fk_appointments_employee_id foreign key(employeeId) references userDetails(id) ON DELETE CASCADE,
    constraint fk_appointments_patient_id foreign key(patientId) references userDetails(id) ON DELETE CASCADE
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
    constraint fk_invoice_employee_id foreign key(employeeId) references userDetails(id) ON DELETE CASCADE,
    constraint fk_invoice_patient_id foreign key(patientId) references userDetails(id) ON DELETE CASCADE,
    constraint fk_invoice_appointment_id foreign key(appointmentId) references appointments(id) ON DELETE CASCADE
);

create table invoiceItem(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    invoiceId integer not null,
    itemCost double not null,
    quantity integer not null,
    description varchar(100) not null,
    constraint fk_invoiceitem_invoice_id foreign key(invoiceId) references invoice(id) ON DELETE CASCADE
);

create table systemSetting(
    settingKey varchar(100) not null primary key,
    settingVal varchar(100) not null
);

create table prescriptions(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key ,
    originatingPrescriptionId INTEGER ,
    employeeId INTEGER NOT NULL,
    patientId INTEGER NOT NULL,
    prescriptionDetails  LONG VARCHAR NOT NULL,
    appointmentId INTEGER NOT NULL,
    issueDate DATE NOT NULL,
    constraint fk_prescriptions_employee_id foreign key(employeeId) references userDetails(id) ON DELETE CASCADE,
    constraint fk_prescriptions_patient_id foreign key(patientId) references userDetails(id) ON DELETE CASCADE,
    constraint fk_prescriptions_appointment_id foreign key(appointmentId) references appointments(id) ON DELETE CASCADE,
    constraint fk_prescriptions_originatingPrescriptionId_id foreign key(originatingPrescriptionId) references prescriptions(id) ON DELETE CASCADE
);

--The order of these are important they link to the above records
insert into systemUser(username, password, usergroup, active)
values ('admin', 'admin', 'ADMIN', true),
       ('receptionist', 'receptionist', 'RECEPTIONIST', true),
       ('doctor', 'doctor', 'DOCTOR', true),
       ('nurse', 'nurse', 'NURSE', true),
       ('pnurse', 'nurse', 'NURSE', true),
       ('patient1', 'patient1', 'NHS_PATIENT', true),
       ('patient2', 'patient2', 'NHS_PATIENT', true),
       ('patient3', 'patient3', 'PRIVATE_PATIENT', true),
       ('patient4', 'patient4', 'PRIVATE_PATIENT', true),
       ('patient5', 'patient5', 'PRIVATE_PATIENT', true);


insert into userDetails(userId, firstName, lastName, addressLine1, addressLine2, addressLine3, town, postCode, dob)
values (1,'Tom', 'Thatcher', '64 East Street', '', '', 'Bristol', 'BS108TY', DATE('1982-05-13')),       --admin @USERGROUP=ADMIN
       (2,'Casandra', 'Smith', '18 Richards Street', '', '', 'Bristol', 'BS078TP', DATE('1955-11-29')), --receptionist @USERGROUP=RECEPTIONIST
       (3,'Dr First', 'Doctor', '52 John Road', '', '', 'Bristol', 'BS098TP', DATE('1980-12-20')),      --doctor @USERGROUP=DOCTOR
       (4,'Sandra', 'Johnson', '64 Albert Street', '', '', 'Bristol', 'BS098TP', DATE('1982-05-13')),   --nurse @USERGROUP=NURSE
       (5,'John', 'tipsy', '64 Albert Street', '', '', 'Bristol', 'BS098TP', DATE('1982-05-13')),   --pnurse @USERGROUP=NURSE
       (6,'Rob', 'Smith', '12 Oak Road', '', '', 'Bristol', 'BS215TP', DATE('1995-02-01')),             --patient1 @USERGROUP=PATIENT
       (7,'Liz', 'Brown', '93 Snowflake Road', '', '', 'Bristol', 'BS45TP', DATE('2001-07-11')),        --patient2 @USERGROUP=PATIENT
       (8,'Jane', 'Jones', '52 Selbrooke Crescent', '', '', 'Bristol', 'BS162PS', DATE('2001-07-11')),  --patient3 @USERGROUP=PATIENT
       (9,'Alfred', 'Blue', '22 Denmark Street', '', '', 'Bristol', 'BS15DQ', DATE('2001-07-11')),      --patient4 @USERGROUP=PATIENT
       (10,'Tim', 'Hesitant', '10 Stoke Park', '', '', 'Bristol', 'BS91LF', DATE('2001-07-11'));         --patient5 @USERGROUP=PATIENT

--working hours
insert into workingHours(workingDays, startTime, endTime)
values('1,2,3,4,5', TIME('9:00:00'), TIME('17:00:00')),
      ('1,5', TIME('9:00:00'), TIME('17:00:00'));

insert into workingHoursJT(employeeId, workingHoursId)
values (3,1),
       (4,1),
       (5,2);

--create appointments
insert into APPOINTMENTS(APPOINTMENTDATE, APPOINTMENTTIME, EMPLOYEEID, PATIENTID, APPOINTMENTSTATUS)
VALUES ('2021-01-21', '14:00:00', 3, 1, 'PENDING'),
       ('2021-01-21', '14:15:00', 3, 1, 'PENDING'),
       ('2021-01-21', '14:30:00', 3, 1, 'PENDING'),
       ('2021-01-21', '14:45:00', 3, 1, 'PENDING'),
       ('2021-01-22', '14:00:00', 3, 1, 'PENDING'),
       ('2021-01-23', '14:15:00', 3, 1, 'PENDING'),
       ('2021-01-24', '14:30:00', 3, 1, 'PENDING'),
       ('2021-01-24', '14:45:00', 3, 1, 'CANCELED');

--insert prescriptions
insert into PRESCRIPTIONS(ORIGINATINGPRESCRIPTIONID, EMPLOYEEID, PATIENTID, PRESCRIPTIONDETAILS, APPOINTMENTID, ISSUEDATE)
values(1, 1, 2, 'some text here', 1, '2020-11-01'),
      (1, 1, 2, 'different text', 1, '2020-11-07'),
      (1, 2, 2, 'some text here', 1, '2020-11-01'),
      (1, 2, 2, 'different text', 1, '2020-11-07');

-- initialise system settings
insert into systemSetting(settingKey, settingVal)
values ('baseConsultationFeeDoctor', '150'),
       ('baseConsultationFeeNurse', '100'),
       ('consultationSlotTime', '10');

--create third party items
insert into thirdParty(thirdPartyName, addressLine1, addressLine2, addressLine3, town, postCode, thirdPartyType, active)
VALUES ('Bristol Royal Infirmary', 'Upper Maudlin St', '', '', 'Bristol', 'BS28HW', 'HOSPITAL', true),
       ('Southmead Hospital', 'Southmead Rd', '', '', 'Bristol', 'BS105NB', 'HOSPITAL', true),
       ('Bristol Endodontic Clinic', '9 North View', '', '', 'Bristol', 'BS67PT', 'SPECIALIST_CLINIC', true),
       ('Bristol Physiotherapy Clinic', 'Workout Harbourside', 'Welsh Back', '', 'Bristol', 'BS14JA', 'SPECIALIST_CLINIC', true);