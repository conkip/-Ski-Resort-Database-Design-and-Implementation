INSERT INTO nathanlamont.Lift VALUES (
    'lift1',
    'Mountain Escape',
    TO_TIMESTAMP('2025-04-15 07:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2025-04-15 19:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    'Intermediate',
    1
);

INSERT INTO nathanlamont.Lift VALUES (
    'lift2',
    'Snow Train',
    TO_TIMESTAMP('2025-04-15 07:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2025-04-15 19:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    'Advanced',
    1
);

INSERT INTO nathanlamont.Lift VALUES (
    'lift3',
    'Bunny Hop',
    TO_TIMESTAMP('2025-04-15 07:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2025-04-15 19:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    'Beginner',
    0
);

commit;
exit;