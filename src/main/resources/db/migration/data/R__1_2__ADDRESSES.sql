INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET) VALUES (-1, 'AGY', 'BRMSYC', 'BUS', 'Y', 'Y', 'Justice Avenue');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET) VALUES (-2, 'AGY', 'WELBYC', 'BUS', 'Y', 'Y', 'Peyton Place');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET, CITY_CODE, COUNTRY_CODE) VALUES (-3, 'AGY', 'BMI', 'BUS', 'Y', 'Y', 'BMI Place', '27913', 'ENG');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET, CITY_CODE, COUNTRY_CODE) VALUES (-4, 'AGY', 'BMI', 'BUS', 'N', 'Y', 'Not Primary Address','27913', 'ENG');
-- blank address
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, PRIMARY_FLAG, MAIL_FLAG, COUNTRY_CODE) VALUES (-5, 'AGY', 'BMI', 'N', 'Y','ENG');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET) VALUES (-6, 'AGY', 'BXI', 'BUS', 'Y', 'Y', 'BXI HMP');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET) VALUES (-7, 'AGY', 'TRO', 'BUS', 'N', 'Y', 'TRO HMP');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET, CITY_CODE) VALUES (-8, 'AGY', 'TRO', 'BUS', 'Y', 'Y', 'Coles Corner', '11129');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET, CITY_CODE) VALUES (-9, 'AGY', 'TRO', 'BUS', 'N', 'Y', 'Coles Corner', '11129');
-- offender addresses
INSERT INTO ADDRESSES (ADDRESS_ID,ADDRESS_TYPE,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-10,'HOME','OFF',-1009,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ENG','Y','N',NULL,'Y',TIMESTAMP '2017-03-01 00:00:00.000000',NULL)
INSERT INTO ADDRESSES (ADDRESS_ID,ADDRESS_TYPE,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-11,'BUS','OFF',-1009,'Flat 1','Brook Hamlets','Mayfield Drive','Nether Edge','25343','S.YORKSHIRE','B5','ENG','N','N',NULL,'N',TIMESTAMP '2015-10-01 00:00:00.000000',NULL)
INSERT INTO ADDRESSES (ADDRESS_ID,ADDRESS_TYPE,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-12,'HOME','OFF',-1009,NULL,'9','Abbydale Road',NULL,'25343','S.YORKSHIRE',NULL,'ENG','N','N','A Comment','N',TIMESTAMP '2014-07-01 00:00:00.000000',NULL)
INSERT INTO ADDRESSES (ADDRESS_ID,ADDRESS_TYPE,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-13,NULL,'OFF',-1009,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ENG','N','N',NULL,'Y',TIMESTAMP '2014-07-01 00:00:00.000000',NULL)
INSERT INTO ADDRESSES (ADDRESS_ID,ADDRESS_TYPE,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-14,'HOME','OFF',-1024,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ENG','Y','N',NULL,'Y',TIMESTAMP '2016-08-02 00:00:00.000000',NULL)


INSERT INTO ADDRESSES (ADDRESS_ID,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-15,'PER',-8,'Flat 1','Brook Hamlets','Mayfield Drive','Nether Edge','25343','S.YORKSHIRE','B5','ENG','Y','N',NULL,'N',TIMESTAMP '2016-08-02 00:00:00.000000',NULL)
INSERT INTO ADDRESSES (ADDRESS_ID,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-16,'PER',-8,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ENG','N','N',NULL,'Y',TIMESTAMP '2016-08-02 00:00:00.000000',NULL)

INSERT INTO ADDRESSES (ADDRESS_ID,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-17,'OFF',-1002,'Flat 1','Brook Hamlets','Mayfield Drive','Nether Edge','25343','S.YORKSHIRE','B5','ENG','Y','N',NULL,'N',TIMESTAMP '2016-08-02 00:00:00.000000','2014-07-01 00:00:00.000000')
INSERT INTO ADDRESSES (ADDRESS_ID,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-18,'OFF_EMP',-1,'Flat 1','Brook Hamlets','Mayfield Drive','Nether Edge','25343','S.YORKSHIRE','B5','ENG','Y','N','Some Comment Text','N',TIMESTAMP '2016-08-02 00:00:00.000000','2014-07-01 00:00:00.000000')

INSERT INTO ADDRESSES (ADDRESS_ID,ADDRESS_TYPE,OWNER_CLASS,OWNER_ID,FLAT,PREMISE,STREET,LOCALITY,CITY_CODE,COUNTY_CODE,POSTAL_CODE,COUNTRY_CODE,PRIMARY_FLAG,MAIL_FLAG,COMMENT_TEXT,NO_FIXED_ADDRESS_FLAG,START_DATE,END_DATE) VALUES (-19,null,'PER',-1009,null,'Another Address','Tree Drive','West Cross','25343','S.YORKSHIRE','B5','ENG','N','N',NULL,'N',TIMESTAMP '2015-10-01 00:00:00.000000',NULL)
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET, CITY_CODE, COUNTRY_CODE) VALUES (-20, 'PER', 'ABDRCT', 'BUS', 'Y', 'Y', 'Bad Data', '27913', 'ENG');
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_CODE, ADDRESS_TYPE, PRIMARY_FLAG, MAIL_FLAG, STREET, CITY_CODE) VALUES (-21, 'AGY', 'ABDRCT', 'BUS', 'Y', 'Y', 'Coles Corner', '11129');

-- offender employment addresses
INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_ID, OWNER_SEQ, OWNER_CODE, ADDRESS_TYPE, FLAT, PREMISE, STREET, LOCALITY, CITY_CODE, COUNTY_CODE, POSTAL_CODE, COUNTRY_CODE, VALIDATED_PAF_FLAG, PRIMARY_FLAG, MAIL_FLAG, CAPACITY, COMMENT_TEXT)
VALUES (2349704, 'OFF_EMP', 584215, 1, null, null, null, null, null, null, '25764', 'STAFFS', null, 'ENG', 'N', 'N', 'N', null, null);

INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_ID, OWNER_SEQ, OWNER_CODE, ADDRESS_TYPE, FLAT, PREMISE, STREET, LOCALITY, CITY_CODE, COUNTY_CODE, POSTAL_CODE, COUNTRY_CODE, VALIDATED_PAF_FLAG, PRIMARY_FLAG, MAIL_FLAG, CAPACITY, COMMENT_TEXT)
VALUES (2349705, 'OFF_EMP', 584215, 1, null, null, null, null, null, null, null, null, null, 'ENG', 'N', 'N', 'N', null, null);

INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_ID, OWNER_SEQ, OWNER_CODE, ADDRESS_TYPE, FLAT, PREMISE, STREET, LOCALITY, CITY_CODE, COUNTY_CODE, POSTAL_CODE, COUNTRY_CODE, VALIDATED_PAF_FLAG, PRIMARY_FLAG, MAIL_FLAG, CAPACITY, COMMENT_TEXT)
VALUES (942100, 'OFF_EMP', 55419, 1, null, null, null, null, null, null, '25764', 'STAFFS', null, 'ENG', 'N', 'N', 'N', null, null);

INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_ID, OWNER_SEQ, OWNER_CODE, ADDRESS_TYPE, FLAT, PREMISE, STREET, LOCALITY, CITY_CODE, COUNTY_CODE, POSTAL_CODE, COUNTRY_CODE, VALIDATED_PAF_FLAG, PRIMARY_FLAG, MAIL_FLAG, CAPACITY, COMMENT_TEXT)
VALUES (942102, 'OFF_EMP', 55419, 1, null, null, null, null, null, null, null, null, null, 'ENG', 'N', 'N', 'N', null, null);

INSERT INTO ADDRESSES (ADDRESS_ID, OWNER_CLASS, OWNER_ID, OWNER_SEQ, OWNER_CODE, ADDRESS_TYPE, FLAT, PREMISE, STREET, LOCALITY, CITY_CODE, COUNTY_CODE, POSTAL_CODE, COUNTRY_CODE, VALIDATED_PAF_FLAG, PRIMARY_FLAG, MAIL_FLAG, CAPACITY, COMMENT_TEXT)
VALUES (2349706, 'OFF_EDU', 584215, 1, null, null, null, null, null, null, '25764', 'STAFFS', null, 'ENG', 'N', 'N', 'N', null, null);