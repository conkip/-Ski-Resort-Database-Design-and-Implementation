CREATE TABLE nathanlamont.Property (
    propertyID INTEGER,
    name VARCHAR2(30),
    address VARCHAR2(30),
    property VARCHAR2(15),
    primary key (PropertyID));
    

CREATE TABLE nathanlamont.Shop (
    shopID INTEGER,
    name VARCHAR2(30),
    shopType VARCHAR2(15),
    buildingID INTEGER,
    income NUMBER(12, 2),
    foreign key (buildingID) references nathanlamont.Property(propertyID),
    primary key (shopID));


CREATE TABLE nathanlamont.Equipment (
    equipmentID INTEGER,
    equipmentType VARCHAR2(15),
    equipmentSize VARCHAR2 (15),
    archived NUMBER(1),
    primary key (equipmentID));

CREATE TABLE nathanlamont.Member (
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

CREATE TABLE nathanlamont.Pass (
    passID varchar2(15),
    memberID INTEGER,
    numUses INTEGER,
    passType VARCHAR2(15),
    price NUMBER(10,2),
    exprDate DATE,
    foreign key (memberID) references nathanlamont.Member(memberID),
    primary key (passID));

CREATE TABLE nathanlamont.Rental (
    rentalID INTEGER,
    equipmentID INTEGER,
    passID varchar2(15),
    rentalTime DATE,
    returnStatus NUMBER(1),
    foreign key (passID) references nathanlamont.Pass(passID),
    foreign key (equipmentID) references nathanlamont.Equipment(equipmentID),
    primary Key (rentalID));

CREATE TABLE nathanlamont.Lift (
    liftID VARCHAR2(15),
    liftName VARCHAR2(15),
    openTime TIMESTAMP,
    closeTime TIMESTAMP,
    abilityLevel VARCHAR2(15),
    status NUMBER(1),
    primary key (liftID)
    );

CREATE TABLE nathanlamont.LiftLog (
    passID varchar2(15),
    liftID VARCHAR2(15),
    dateTime Date,
    foreign key (passID) references nathanlamont.Pass(passID),
    foreign key (liftID) references nathanlamont.Lift(liftID));

CREATE TABLE nathanlamont.Trail (
    name VARCHAR2(15),
    category VARCHAR2(15),
    startPos VARCHAR2(15),
    endPos VARCHAR2(15),
    status NUMBER(1),
    difficulty VARCHAR2(15),
    primary key (name));

CREATE TABLE nathanlamont.TrailLift (
    liftID VARCHAR2(15),
    trailName VARCHAR2(15),
    foreign key (trailName) references nathanlamont.Trail(name),
    foreign key (liftID) references nathanlamont.Lift(liftID)
    );

CREATE TABLE nathanlamont.Employee (
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
    propertyID INTEGER,
    foreign key (propertyID) references nathanlamont.Property(propertyID),
    primary key (employeeID)
    );

CREATE TABLE nathanlamont.LessonOffering (
    lessonID INTEGER,
    instructorID VARCHAR2(15),
    lessonType VARCHAR2(15),
    skillLevel VARCHAR2(15),
    schedule VARCHAR2(15),
    foreign key (instructorID) references nathanlamont.Employee(employeeID),
    primary key(lessonID));

CREATE TABLE nathanlamont.LessonPurchase (
    orderID INTEGER,
    memberID INTEGER,
    lessonID INTEGER,
    sessionsPurchased NUMBER(5),
    remainingSessions NUMBER(5),
    price NUMBER(5,2),
    foreign key (memberID) references nathanlamont.Member(memberID),
    foreign key (lessonID) references nathanlamont.LessonOffering(lessonID),
    primary key (orderID)
    );

CREATE TABLE nathanlamont.LessonLog (
    orderID INTEGER,
    dateTime DATE,
    foreign key (orderID) references nathanlamont.LessonPurchase(orderID)
    );


CREATE TABLE nathanlamont.Updates (
    updateType varchar2(15), -- delete or update
    tableChanged varchar2(25), -- table affected
    changeID varchar(25), --pk of whatever was changed
    dateTime DATE
);

commit;

GRANT ALL ON nathanlamont.Property TO PUBLIC;
GRANT ALL ON nathanlamont.Shop TO PUBLIC;
GRANT ALL ON nathanlamont.Equipment TO PUBLIC;
GRANT ALL ON nathanlamont.Rental TO PUBLIC;
GRANT ALL ON nathanlamont.Pass TO PUBLIC;
GRANT ALL ON nathanlamont.LiftLog TO PUBLIC;
GRANT ALL ON nathanlamont.Lift TO PUBLIC;
GRANT ALL ON nathanlamont.TrailLift TO PUBLIC;
GRANT ALL ON nathanlamont.Trail TO PUBLIC;
GRANT ALL ON nathanlamont.Member TO PUBLIC;
GRANT ALL ON nathanlamont.LessonLog TO PUBLIC;
GRANT ALL ON nathanlamont.LessonPurchase TO PUBLIC;
GRANT ALL ON nathanlamont.LessonOffering TO PUBLIC;
GRANT ALL ON nathanlamont.Employee TO PUBLIC;
GRANT ALL ON nathanlamont.Updates TO PUBLIC;

commit;

exit;