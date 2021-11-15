-----------------------------
-- OFFENDER_SENTENCE_TERMS --
-----------------------------

-- Single 'IMP' record
INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-1, 1, 1, 'IMP', 3, null, TO_DATE('2017-03-25', 'YYYY-MM-DD'), TO_DATE('2020-03-24', 'YYYY-MM-DD'), 'N');

-- Multiple 'IMP' records - earliest start date should be used
INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-2, 1, 1, 'IMP', null, 6, TO_DATE('2016-11-22', 'YYYY-MM-DD'), TO_DATE('2017-05-21', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-2, 2, 1, 'IMP', 2, null, TO_DATE('2017-05-22', 'YYYY-MM-DD'), TO_DATE('2017-06-21', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, WEEKS, DAYS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
VALUES (-2, 2, 2, 'IMP', 2, 3, TO_DATE('2017-06-22', 'YYYY-MM-DD'), TO_DATE('2019-07-21', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
VALUES (-2, 2, 3, 'IMP', 25, TO_DATE('2017-07-22', 'YYYY-MM-DD'), TO_DATE('2019-05-21', 'YYYY-MM-DD'), 'Y');

-- Multiple records with different SENTENCE_TERM_CODE values - start date should come from record having 'IMP' sentence term code
INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-3, 1, 1, 'LIC', null, 2, TO_DATE('2016-11-05', 'YYYY-MM-DD'), TO_DATE('2017-01-04', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-3, 2, 1, 'IMP', 5, null, TO_DATE('2015-03-16', 'YYYY-MM-DD'), TO_DATE('2020-03-15', 'YYYY-MM-DD'), 'N');

-- Single 'IMP' record
INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-4, 1, 1, 'IMP', 15, null, TO_DATE('2007-10-16', 'YYYY-MM-DD'), TO_DATE('2022-10-15', 'YYYY-MM-DD'), 'N');

-- 2 sentences, the 2nd with 2 terms
INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-5, 1, 1, 'IMP', 6, 6, TO_DATE('2017-02-08', 'YYYY-MM-DD'), TO_DATE('2023-08-07', 'YYYY-MM-DD'), 'N');
INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-5, 2, 1, 'IMP', 3, 2, TO_DATE('2023-08-07', 'YYYY-MM-DD'), TO_DATE('2026-10-07', 'YYYY-MM-DD'), 'N');
INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-5, 2, 2, 'IMP', 0, 4, TO_DATE('2023-09-17', 'YYYY-MM-DD'), TO_DATE('2026-10-07', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-6, 1, 1, 'IMP', null, 9, TO_DATE('2017-09-01', 'YYYY-MM-DD'), TO_DATE('2018-05-31', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-7, 1, 1, 'IMP', null, 9, TO_DATE('2017-09-01', 'YYYY-MM-DD'), TO_DATE('2018-05-31', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-8, 1, 1, 'IMP', null, 9, TO_DATE('2017-09-01', 'YYYY-MM-DD'), TO_DATE('2018-05-31', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-9, 1, 1, 'IMP', null, 9, TO_DATE('2017-09-01', 'YYYY-MM-DD'), TO_DATE('2018-05-31', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-10, 1, 1, 'IMP', null, 9, TO_DATE('2017-09-01', 'YYYY-MM-DD'), TO_DATE('2018-05-31', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-11, 1, 1, 'IMP', null, 9, TO_DATE('2017-09-01', 'YYYY-MM-DD'), TO_DATE('2018-05-31', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-12, 1, 1, 'IMP', null, 9, TO_DATE('2017-09-01', 'YYYY-MM-DD'), TO_DATE('2018-05-31', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-17, 1, 1, 'IMP', 10, null, TO_DATE('2015-05-05', 'YYYY-MM-DD'), TO_DATE('2025-05-14', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-18, 1, 1, 'IMP', 4, 9, TO_DATE('2016-11-17', 'YYYY-MM-DD'), TO_DATE('2021-08-16', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
VALUES (-20, 1, 1, 'IMP', 3, null, TO_DATE('2017-03-25', 'YYYY-MM-DD'), TO_DATE('2020-03-24', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
VALUES (-20, 1, 2, 'IMP', 3, null, TO_DATE('2017-03-25', 'YYYY-MM-DD'), TO_DATE('2020-03-24', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
VALUES (-20, 2, 2, 'IMP', 5, null, TO_DATE('2017-03-25', 'YYYY-MM-DD'), TO_DATE('2020-03-24', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-24, 1, 1, 'IMP', 7, 0, TO_DATE('2017-07-07', 'YYYY-MM-DD'), TO_DATE('2024-07-06', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-25, 1, 1, 'IMP', 20, 0, TO_DATE('2009-09-09', 'YYYY-MM-DD'), TO_DATE('2029-09-08', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-27, 1, 1, 'IMP', 25, 0, TO_DATE('2014-09-09', 'YYYY-MM-DD'), TO_DATE('2039-09-08', 'YYYY-MM-DD'), 'Y');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-28, 1, 1, 'IMP', 25, 0, TO_DATE('2014-09-09', 'YYYY-MM-DD'), TO_DATE('2039-09-08', 'YYYY-MM-DD'), 'Y');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-29, 1, 1, 'IMP', 6, 6, TO_DATE('2017-02-08', 'YYYY-MM-DD'), TO_DATE('2023-08-07', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
  VALUES (-30, 1, 1, 'IMP', 15, null, TO_DATE('2007-10-16', 'YYYY-MM-DD'), TO_DATE('2022-10-15', 'YYYY-MM-DD'), 'N');


INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
VALUES (-31, 2, 1, 'IMP', 15, null, TO_DATE('2007-10-16', 'YYYY-MM-DD'), TO_DATE('2022-10-15', 'YYYY-MM-DD'), 'N');

INSERT INTO OFFENDER_SENTENCE_TERMS (OFFENDER_BOOK_ID, SENTENCE_SEQ, TERM_SEQ, SENTENCE_TERM_CODE, YEARS, MONTHS, START_DATE, END_DATE, LIFE_SENTENCE_FLAG)
VALUES (-31, 2, 2, 'LIC', 5, null, TO_DATE('2007-10-16', 'YYYY-MM-DD'), TO_DATE('2022-10-15', 'YYYY-MM-DD'), 'N');

