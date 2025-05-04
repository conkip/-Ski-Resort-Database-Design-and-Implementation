CREATE TABLE group14.Property (
    propertyID INTEGER,
    name VARCHAR2(30),
    adress VARCHAR2(30),
    property VARCHAR2(15),
    primary key (PropertyID));

CREATE TABLE group14.Shop (
    shopID INTEGER,
    name VARCHAR2(30),
    type VARCHAR2(15),
    buildingID INTEGER,
    income NUMBER(12, 2),
    primary key (shopID));


CREATE TABLE group14.Equipment (
    equipmentID INTEGER,
    type VARCHAR2(15),
    size VARCHAR2 (15),
    availableQty INTEGER,
    primary key (equipmentID));

CREATE TABLE group14.Rental (
    rentalID INTEGER,
    equipmentID INTEGER,
    passID varchar2(15),
    rentalTime DATE,
    returnStatus NUMBER(1),
    primary Key (rentalID));

CREATE TABLE group14.Pass (
    passID varchar2(15),
    memberID INTEGER,
    numUses INTEGER,
    passType VARCHAR2(15),
    price NUMBER(10,2),
    exprDATE DATE,
    primary key (passID));

CREATE TABLE group14.LiftLog (
    passID varchar2(15),
    liftID INTEGER,
    dateTime DATE);

CREATE TABLE group14.Lift (
    liftID VARCHAR2(15),
    liftName VARCHAR2(15),
    openTime TIMESTAMP,
    closeTime TIMESTAMP,
    abilityLevel VARCHAR2(15),
    primary key (liftID)
    );

CREATE TABLE group14.Trail (
    name VARCHAR2(15),
    category VARCHAR2(15),
    start VARCHAR2(15),
    end VARCHAR2(15),
    status NUMBER(1),
    difficulty VARCHAR2(15),
    liftID VARCHAR2(15),
    primary key (name));

CREATE TABLE group14.Member (
    memberID INTEGER,
    name VARCHAR2(15),
    phoneNUMBER VARCHAR2(15),
    email VARCHAR2(25),
    dateBirth DATE,
    emergencyName VARCHAR2(15),
    emergencyPhone VARCHAR2(15),
    emergencyEmail VARCHAR2(15),
    primary key (memberID)
    );

CREATE TABLE group14.LessonLog (
    orderID INTEGER,
    dateTime DATE
    );

CREATE TABLE group14.LessonPurchase (
    orderID INTEGER,
    memberID INTEGER,
    lessonID INTEGER,
    sessionsPurchased NUMBER(5),
    remainingSessions NUMBER(5),
    price NUMBER(5,2),
    primary key (orderID)
    );

CREATE TABLE group14.LessonOffering (
    lessonID INTEGER,
    instructorID VARCHAR2(15),
    type VARCHAR2(15),
    level VARCHAR2(15),
    schedule VARCHAR2(15),
    primary key(lessonID));

CREATE TABLE group14.Employee (
    employeeID VARCHAR2(15),
    name VARCHAR2(15),
    phone VARCHAR2(15),
    email VARCHAR2(25),
    position VARCHAR2(15),
    monthlySalary NUMBER(7,2),
    startDate DATE,
    gender VARCHAR2(15),
    ethnicity VARCHAR2(15),
    dateBirth DATE,
    primary key (employeeID)
    );

commit;