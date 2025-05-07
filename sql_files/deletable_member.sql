-- Step 1: Insert into Property (new one)
INSERT INTO nathanlamont.Property (propertyID, name, address, property)
VALUES (302, 'Cedar Ridge', '321 Forest Drive', 'Cabin');

-- Step 2: Insert into Employee (new instructor at the new property)
INSERT INTO nathanlamont.Employee (
    employeeID, name, phone, email, position, monthlySalary, startDate,
    gender, ethnicity, dateBirth, propertyID
) VALUES (
    'EMP401', 'Casey Rivers', '5551234567', 'casey.rivers@resort.com', 'Instructor',
    5200.00, DATE '2022-11-05', 'Female', 'Hispanic', DATE '1990-07-22', 302
);

-- Step 3: Insert into Member
INSERT INTO nathanlamont.Member (
    memberID, name, phoneNUMBER, email, dateBirth,
    emergencyName, emergencyPhone, emergencyEmail
) VALUES (
    40002, 'Sage Willow', '5557654321', 'sage.willow@email.com', DATE '1988-10-04',
    'Rowan Willow', '5559991234', 'rowan.willow@email.com'
);

-- Step 4: Insert into Pass
INSERT INTO nathanlamont.Pass (
    passID, memberID, numUses, passType, price, exprDate
) VALUES (
    'PASS40002', 40002, 20, 'Annual', 950.00, DATE '2024-12-31'
);

-- Step 5: Insert into Equipment
INSERT INTO nathanlamont.Equipment (
    equipmentID, equipmentType, equipmentSize, archived
) VALUES (
    402, 'Snowboard', 'L', 0
);

-- Step 6: Insert into Rental
INSERT INTO nathanlamont.Rental (
    rentalID, equipmentID, passID, rentalTime, returnStatus
) VALUES (
    5002, 402, 'PASS40002', DATE '2024-02-10', 1
);

-- Step 7: Insert into Lift
INSERT INTO nathanlamont.Lift (
    liftID, liftName, openTime, closeTime, abilityLevel, status
) VALUES (
    'LIFT1002', 'Eagle Cliff Lift',
    TIMESTAMP '2025-01-01 08:30:00', TIMESTAMP '2025-01-01 16:30:00',
    'Advanced', 1
);

-- Step 8: Insert into LiftLog
INSERT INTO nathanlamont.LiftLog (
    passID, liftID, dateTime
) VALUES (
    'PASS40002', 'LIFT1002', DATE '2025-02-10'
);

-- Step 9: Insert into LessonOffering
INSERT INTO nathanlamont.LessonOffering (
    lessonID, instructorID, lessonType, skillLevel, schedule
) VALUES (
    9002, 'EMP401', 'Freestyle Tricks', 'Advanced', 'Fridays'
);

-- Step 10: Insert into LessonPurchase
INSERT INTO nathanlamont.LessonPurchase (
    orderID, memberID, lessonID, sessionsPurchased, remainingSessions, price
) VALUES (
    10002, 40002, 9002, 4, 1, 400.00
);

-- Step 11: Insert into LessonLog
INSERT INTO nathanlamont.LessonLog (
    orderID, dateTime
) VALUES (
    10002, DATE '2025-02-14'
);

-- Step 12: Finalize
COMMIT;
