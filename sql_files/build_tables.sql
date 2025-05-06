CREATE TABLE group14.Property (
    propertyID INTEGER,
    name VARCHAR2(30),
    address VARCHAR2(30),
    property VARCHAR2(15),
    primary key (PropertyID));
    

CREATE TABLE group14.Shop (
    shopID INTEGER,
    name VARCHAR2(30),
    type VARCHAR2(15),
    buildingID INTEGER,
    income NUMBER(12, 2),
    foreign key (buildingID) references group14.Property(propertyID),
    primary key (shopID));


CREATE TABLE group14.Equipment (
    equipmentID INTEGER,
    type VARCHAR2(15),
    size VARCHAR2 (15),
    archived NUMBER(1),
    primary key (equipmentID));

CREATE TABLE group14.Rental (
    rentalID INTEGER,
    equipmentID INTEGER,
    passID varchar2(15),
    rentalTime DATE,
    returnStatus NUMBER(1),
    foreign key (passID) references group14.Pass(passID),
    foreign key (equipmentID) references group14.Equipment(equipmentID),
    primary Key (rentalID));

CREATE TABLE group14.Pass (
    passID varchar2(15),
    memberID INTEGER,
    numUses INTEGER,
    passType VARCHAR2(15),
    price NUMBER(10,2),
    exprDATE DATE,
    foreign key (memberID) references group14.Member(memberID),
    primary key (passID));

CREATE TABLE group14.LiftLog (
    passID varchar2(15),
    liftID INTEGER,
    foreign key (passID) references group14.Pass(passID),
    foreign key (liftID) references group14.Lift(liftID),
    dateTime DATE);

CREATE TABLE group14.Lift (
    liftID VARCHAR2(15),
    liftName VARCHAR2(15),
    openTime TIMESTAMP,
    closeTime TIMESTAMP,
    abilityLevel VARCHAR2(15),
    status NUMBER(1),
    primary key (liftID)
    );

CREATE TABLE group14.TrailLift (
    liftID VARCHAR2(15),
    trailName VARCHAR2(15),
    foreign key (trailName) references group14.Trail(name),
    foreign key (liftID) references group14.Lift(liftID)
    );

CREATE TABLE group14.Trail (
    name VARCHAR2(15),
    category VARCHAR2(15),
    start VARCHAR2(15),
    end VARCHAR2(15),
    status NUMBER(1),
    difficulty VARCHAR2(15),
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
    dateTime DATE,
    foreign key (orderID) references group14.LessonPurchase(orderID)
    );

CREATE TABLE group14.LessonPurchase (
    orderID INTEGER,
    memberID INTEGER,
    lessonID INTEGER,
    sessionsPurchased NUMBER(5),
    remainingSessions NUMBER(5),
    price NUMBER(5,2),
    foreign key (memberID) references group14.Member(memberID),
    foreign key (lessonID) references group14.LessonOffering(lessonID),
    primary key (orderID)
    );

CREATE TABLE group14.LessonOffering (
    lessonID INTEGER,
    instructorID VARCHAR2(15),
    type VARCHAR2(15),
    level VARCHAR2(15),
    schedule VARCHAR2(15),
    foreign key (instructorID) references group14.Employee(employeeID),
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
    propertyID INTEGER,
    foreign key (propertyID) references group14.Property(propertyID),
    primary key (employeeID)
    );

CREATE TABLE group14.Updates (
    updateType varchar2(15), -- delete or update
    tableChanged varchar2(25), -- table affected
    changeID varchar(25), --pk of whatever was changed
    dateTime DATE
);

commit;

GRANT ALL ON group14.Property TO PUBLIC;
GRANT ALL ON group14.Shop TO PUBLIC;
GRANT ALL ON group14.Equipment TO PUBLIC;
GRANT ALL ON group14.Rental TO PUBLIC;
GRANT ALL ON group14.Pass TO PUBLIC;
GRANT ALL ON group14.LiftLog TO PUBLIC;
GRANT ALL ON group14.Lift TO PUBLIC;
GRANT ALL ON group14.TrailLift TO PUBLIC;
GRANT ALL ON group14.Trail TO PUBLIC;
GRANT ALL ON group14.Member TO PUBLIC;
GRANT ALL ON group14.LessonLog TO PUBLIC;
GRANT ALL ON group14.LessonPurchase TO PUBLIC;
GRANT ALL ON group14.LessonOffering TO PUBLIC;
GRANT ALL ON group14.Employee TO PUBLIC;
GRANT ALL ON group14.Updates TO PUBLIC;

commit;

exit;