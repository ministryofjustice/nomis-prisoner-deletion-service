
  CREATE TABLE "OFFENDER_NO_PAY_PERIODS"
   (    "OFFENDER_BOOK_ID" NUMBER(10,0) NOT NULL,
    "START_DATE" DATE NOT NULL,
    "END_DATE" DATE,
    "REASON_CODE" VARCHAR2(12 CHAR) NOT NULL,
    "COMMENT_TEXT" VARCHAR2(40 CHAR),
    "OFFENDER_NO_PAY_PERIOD_ID" NUMBER(10,0) NOT NULL,
    "CREATE_DATETIME" TIMESTAMP (9) DEFAULT systimestamp NOT NULL,
    "CREATE_USER_ID" VARCHAR2(32 CHAR) DEFAULT USER NOT NULL,
    "MODIFY_DATETIME" TIMESTAMP (9),
    "MODIFY_USER_ID" VARCHAR2(32 CHAR),
    "AUDIT_TIMESTAMP" TIMESTAMP (9),
    "AUDIT_USER_ID" VARCHAR2(32 CHAR),
    "AUDIT_MODULE_NAME" VARCHAR2(65 CHAR),
    "AUDIT_CLIENT_USER_ID" VARCHAR2(64 CHAR),
    "AUDIT_CLIENT_IP_ADDRESS" VARCHAR2(39 CHAR),
    "AUDIT_CLIENT_WORKSTATION_NAME" VARCHAR2(64 CHAR),
    "AUDIT_ADDITIONAL_INFO" VARCHAR2(256 CHAR),
     CONSTRAINT "OFFENDER_NO_PAY_PERIODS_PK" PRIMARY KEY ("OFFENDER_NO_PAY_PERIOD_ID"),
  );

  CREATE UNIQUE INDEX "OFFENDER_NO_PAY_PERIODS_PK" ON "OFFENDER_NO_PAY_PERIODS" ("OFFENDER_NO_PAY_PERIOD_ID");


  CREATE UNIQUE INDEX "OFFENDER_NO_PAY_PERIODS_UK" ON "OFFENDER_NO_PAY_PERIODS" ("OFFENDER_BOOK_ID", "START_DATE", "END_DATE");
