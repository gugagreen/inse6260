CREATE TABLE USER(
	USERNAME VARCHAR(255) NOT NULL PRIMARY KEY,
	PASSWORD VARCHAR(255) NOT NULL,
	ENABLED BOOLEAN DEFAULT TRUE NOT NULL,
	TYPE VARCHAR(255)
);

CREATE TABLE USER_ROLE(
	USER_USERNAME VARCHAR(255) NOT NULL,
	USER_ROLE_ID VARCHAR(255) NOT NULL,
	PRIMARY KEY(USER_USERNAME,USER_ROLE_ID),
	FOREIGN KEY fk_user(USER_USERNAME) REFERENCES USER(USERNAME) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE STUDENT(
	USERNAME VARCHAR(255) NOT NULL,
	ORIGIN VARCHAR(255) NOT NULL,
	FOREIGN KEY fk_user(USERNAME) REFERENCES USER(USERNAME) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE COURSE(
	ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	CODE VARCHAR(255) NOT NULL,
	CREDITS INTEGER,
	DESCRIPTION VARCHAR(255),
	NAME VARCHAR(255),
	CONSTRAINT uk_course_code UNIQUE(CODE)
);

CREATE TABLE COURSE_DATES(
	ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	SEASON VARCHAR(255) NOT NULL,
	WEEK_DAYS VARCHAR(255) NOT NULL,
	START_DATE DATE NOT NULL,
	START_TIME TIME NOT NULL,
	END_DATE DATE NOT NULL,
	END_TIME TIME NOT NULL,
	DISC_DATE DATE NOT NULL
);

CREATE TABLE COURSE_ENTRY(
	ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	SIZE INTEGER NOT NULL,
	COURSE_ID BIGINT NOT NULL,
	BASE_COST decimal(19,2) DEFAULT NULL,
	DATE_ID BIGINT,
	PROFESSOR_USERNAME VARCHAR(255),
	FOREIGN KEY fk_course(COURSE_ID) REFERENCES COURSE(ID) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY fk_date(DATE_ID) REFERENCES COURSE_DATES(ID) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY fk_professor(PROFESSOR_USERNAME) REFERENCES USER(USERNAME) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE COURSE_ENTRY_STUDENTS(
	COURSE_ENTRY_ID BIGINT NOT NULL,
	STUDENTS_USERNAME VARCHAR(255) NOT NULL,
	FOREIGN KEY fk_course(COURSE_ENTRY_ID) REFERENCES COURSE_ENTRY(ID) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY fk_student(STUDENTS_USERNAME) REFERENCES USER(USERNAME) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE ACADEMIC_RECORD_ENTRY (
	ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	GRADE VARCHAR(255) NOT NULL,
	STATUS VARCHAR(255) NOT NULL,
	COURSE_ENTRY_ID BIGINT NOT NULL,
	FOREIGN KEY fk_course_entry(COURSE_ENTRY_ID) REFERENCES COURSE_ENTRY (ID) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE STUDENT_ACADEMIC_RECORDS (
	STUDENT_USERNAME varchar(255) NOT NULL,
	ACADEMIC_RECORDS_ID bigint NOT NULL UNIQUE,
	FOREIGN KEY fk_user(STUDENT_USERNAME) REFERENCES STUDENT (USERNAME),
	FOREIGN KEY fk_academic_record(ACADEMIC_RECORDS_ID) REFERENCES ACADEMIC_RECORD_ENTRY (ID)
);

CREATE TABLE PAYMENT (
	ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	VALUE decimal(19,2) DEFAULT NULL,
	DATE DATE
);

CREATE TABLE STUDENT_PAYMENTS (
	STUDENT_USERNAME varchar(255) NOT NULL,
	PAYMENTS_ID bigint NOT NULL UNIQUE,
	FOREIGN KEY fk_user(STUDENT_USERNAME) REFERENCES STUDENT (USERNAME),
	FOREIGN KEY fk_academic_record(PAYMENTS_ID) REFERENCES PAYMENT (ID)
);

