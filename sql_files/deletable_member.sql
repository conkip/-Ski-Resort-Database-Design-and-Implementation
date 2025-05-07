-- Property
INSERT INTO nathanlamont.Property (propertyID, name, address, property)
VALUES (1, 'Main Lodge', '123 Mountain Rd', 'Building');

-- Shop
INSERT INTO nathanlamont.Shop (shopID, name, shopType, buildingID, income)
VALUES (1, 'Ski Gear', 'Rental', 1, 5000.00);

-- Equipment
INSERT INTO nathanlamont.Equipment (equipmentID, equipmentType, equipmentSize, archived)
VALUES (1, 'Ski', 'M', 0);

-- Member
INSERT INTO nathanlamont.Member (
    memberID, name, phoneNumber, email, dateBirth,
    emergencyName, emergencyPhone, emergencyEmail)
VALUES (
    222, 'John Doe', '555-1234', 'johndoe@email.com', TO_DATE('1990-05-20', 'YYYY-MM-DD'),
    'Jane Doe', '555-5678', 'janedoe@email.com'
);

-- Pass
INSERT INTO nathanlamont.Pass (
    passID, memberID, numUses, passType, price, exprDate)
VALUES (
    'P222', 222, 5, 'Season', 299.99, TO_DATE('2024-12-31', 'YYYY-MM-DD')
);

-- Rental
INSERT INTO nathanlamont.Rental (
    rentalID, equipmentID, passID, rentalTime, returnStatus)
VALUES (
    1, 1, 'P222', TO_DATE('2024-12-01', 'YYYY-MM-DD'), 1
);

-- Lift
INSERT INTO nathanlamont.Lift (
    liftID, liftName, openTime, closeTime, abilityLevel, status)
VALUES (
    'L1', 'Summit Express', TO_TIMESTAMP('08:00:00', 'HH24:MI:SS'),
    TO_TIMESTAMP('16:00:00', 'HH24:MI:SS'), 'Intermediate', 1
);

-- LiftLog
INSERT INTO nathanlamont.LiftLog (
    passID, liftID, dateTime)
VALUES (
    'P222', 'L1', TO_DATE('2024-12-01', 'YYYY-MM-DD')
);

-- Trail
INSERT INTO nathanlamont.Trail (
    name, category, startPos, endPos, status, difficulty)
VALUES (
    'Bear Claw', 'Ski', 'Top', 'Base', 1, 'Intermediate'
);

-- TrailLift
INSERT INTO nathanlamont.TrailLift (
    liftID, trailName)
VALUES (
    'L1', 'Bear Claw'
);

-- Employee (Instructor)
INSERT INTO nathanlamont.Employee (
    employeeID, name, phone, email, position, monthlySalary,
    startDate, gender, ethnicity, dateBirth, propertyID)
VALUES (
    'E100', 'Alice Smith', '555-9999', 'asmith@email.com', 'Instructor', 4000.00,
    TO_DATE('2020-06-01', 'YYYY-MM-DD'), 'Female', 'Caucasian', TO_DATE('1985-02-15', 'YYYY-MM-DD'), 1
);

-- LessonOffering
INSERT INTO nathanlamont.LessonOffering (
    lessonID, instructorID, lessonType, skillLevel, schedule)
VALUES (
    1, 'E100', 'Group', 'Beginner', 'Weekly'
);

-- LessonPurchase
INSERT INTO nathanlamont.LessonPurchase (
    orderID, memberID, lessonID, sessionsPurchased, remainingSessions, price)
VALUES (
    1, 222, 1, 5, 0, 150.00
);

-- LessonLog
INSERT INTO nathanlamont.LessonLog (
    orderID, dateTime)
VALUES (
    1, TO_DATE('2024-12-01', 'YYYY-MM-DD')
);

COMMIT;