---------------------------------------------------
-- Seed data for Visits (VISIT) Scheduled Events --
---------------------------------------------------

-- OFFENDER_VISITS (record of scheduled visits)
-- NB: Dates deliberately out of sequence (to allow default sorting to be verified)
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID, COMMENT_TEXT, VISITOR_CONCERN_TEXT) VALUES (-1, -1, TO_DATE('2017-09-12', 'YYYY-MM-DD'), TO_DATE('2017-09-12 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-09-12 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI', 'Some Comment Text', 'Some Visitor Concern Text');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-2, -1, TO_DATE('2017-11-13', 'YYYY-MM-DD'), TO_DATE('2017-11-13 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-11-13 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-3, -1, TO_DATE('2017-12-10', 'YYYY-MM-DD'), TO_DATE('2017-12-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-12-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-4, -1, TO_DATE('2017-10-13', 'YYYY-MM-DD'), TO_DATE('2017-10-13 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-10-13 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-5, -1, TO_DATE('2017-09-15', 'YYYY-MM-DD'), TO_DATE('2017-09-15 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-09-15 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'OFFI', 'SCH', -25,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-6, -1, TO_DATE('2017-09-10', 'YYYY-MM-DD'), TO_DATE('2017-09-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-09-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-7, -1, TO_DATE('2017-07-10', 'YYYY-MM-DD'), TO_DATE('2017-07-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-07-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-8, -1, TO_DATE('2017-08-10', 'YYYY-MM-DD'), TO_DATE('2017-08-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-08-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-9, -1, TO_DATE('2017-05-10', 'YYYY-MM-DD'), TO_DATE('2017-05-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-05-10 16:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'OFFI', 'SCH', -25,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-10, -1, TO_DATE('2017-06-10', 'YYYY-MM-DD'), TO_DATE('2017-06-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-06-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'CANC', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-11, -1, TO_DATE('2017-01-10', 'YYYY-MM-DD'), TO_DATE('2017-01-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-01-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-12, -1, TO_DATE('2017-02-10', 'YYYY-MM-DD'), TO_DATE('2017-02-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-02-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-13, -1, TO_DATE('2017-04-10', 'YYYY-MM-DD'), TO_DATE('2017-04-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-04-10 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-14, -1, TO_DATE('2017-03-10', 'YYYY-MM-DD'), TO_DATE('2017-03-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-03-10 16:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'OFFI', 'SCH', -25,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-15, -1, TO_DATE('2016-12-11', 'YYYY-MM-DD'), TO_DATE('2016-12-11 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2016-12-11 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28,  'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-16, -2, TO_DATE('2017-10-10', 'YYYY-MM-DD'), TO_DATE('2017-10-10 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-10-10 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'OFFI', 'SCH', -25,  'LEI');

-- Last visit
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-26, -4, TO_DATE('2017-10-10', 'YYYY-MM-DD'), TO_DATE('2017-10-10 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2017-10-10 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'OFFI', 'SCH', -27,  'LEI');

INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-17, -3, trunc(sysdate), trunc(sysdate) + INTERVAL '0' HOUR, trunc(sysdate) + INTERVAL '0' HOUR, 'OFFI', 'SCH', -25, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-18, -3, trunc(sysdate), trunc(sysdate) + INTERVAL '1' HOUR, trunc(sysdate) + INTERVAL '1' HOUR, 'SCON', 'SCH', -28, 'LEI');

-- Next visits
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID,OFFENDER_BOOK_ID,VISIT_DATE,START_TIME,END_TIME,VISIT_TYPE,VISIT_STATUS,VISIT_INTERNAL_LOCATION_ID,AGY_LOC_ID) VALUES (-20, -3, trunc(sysdate), trunc(sysdate) + INTERVAL '14' HOUR, trunc(sysdate) + INTERVAL '15' HOUR, 'SCON', 'CAN', -26, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID,OFFENDER_BOOK_ID,VISIT_DATE,START_TIME,END_TIME,VISIT_TYPE,VISIT_STATUS,VISIT_INTERNAL_LOCATION_ID,AGY_LOC_ID) VALUES (-19, -3, trunc(sysdate) + INTERVAL '1' DAY, trunc(sysdate) + INTERVAL '1' DAY + INTERVAL '10' HOUR, trunc(sysdate) + INTERVAL '1' DAY + INTERVAL '11' HOUR, 'SCON', 'SCH', -26, 'LEI');


INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-21, -3, trunc(sysdate) + INTERVAL '2' DAY, trunc(sysdate) + INTERVAL '2' DAY, trunc(sysdate) + INTERVAL '2' DAY, 'SCON', 'SCH', -29, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-22, -3, trunc(sysdate) + INTERVAL '4' DAY, trunc(sysdate) + INTERVAL '4' DAY, trunc(sysdate) + INTERVAL '4' DAY, 'SCON', 'SCH', -13, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-23, -3, trunc(sysdate) + INTERVAL '8' DAY, trunc(sysdate) + INTERVAL '8' DAY, trunc(sysdate) + INTERVAL '8' DAY, 'SCON', 'SCH', -26, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-24, -3, trunc(sysdate) + INTERVAL '10' DAY, trunc(sysdate) + INTERVAL '10' DAY, trunc(sysdate) + INTERVAL '10' DAY, 'SCON', 'SCH', -27, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-25, -3, trunc(sysdate) + INTERVAL '16' DAY, trunc(sysdate) + INTERVAL '16' DAY, trunc(sysdate) + INTERVAL '16' DAY, 'SCON', 'SCH', -28, 'LEI');


INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-28, -5, trunc(sysdate), trunc(sysdate) + 10/24, trunc(sysdate) + 15/24, 'OFFI', 'SCH', -25, 'LEI');

INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-30, -6, TO_DATE('2019-06-29', 'YYYY-MM-DD'), TO_DATE('2019-06-29 01:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-06-29 01:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-31, -6, TO_DATE('2019-06-29', 'YYYY-MM-DD'), TO_DATE('2019-06-29 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-06-29 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'CAN', -26, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-32, -6, TO_DATE('2019-06-30', 'YYYY-MM-DD'), TO_DATE('2019-06-30 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-06-30 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -26, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-33, -6, TO_DATE('2019-07-01', 'YYYY-MM-DD'), TO_DATE('2019-07-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-07-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -29, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-34, -6, TO_DATE('2019-07-03', 'YYYY-MM-DD'), TO_DATE('2019-07-03 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-07-03 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -13, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-35, -6, TO_DATE('2019-07-07', 'YYYY-MM-DD'), TO_DATE('2019-07-07 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-07-07 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -26, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-36, -6, TO_DATE('2019-07-09', 'YYYY-MM-DD'), TO_DATE('2019-07-09 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-07-09 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -27, 'LEI');
INSERT INTO OFFENDER_VISITS (OFFENDER_VISIT_ID, OFFENDER_BOOK_ID, VISIT_DATE, START_TIME, END_TIME, VISIT_TYPE, VISIT_STATUS, VISIT_INTERNAL_LOCATION_ID, AGY_LOC_ID) VALUES (-37, -6, TO_DATE('2019-07-15', 'YYYY-MM-DD'), TO_DATE('2019-07-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2019-07-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'SCON', 'SCH', -28, 'LEI');