
  CREATE TABLE "MERGE_TRANSACTIONS"
   (    "MERGE_TRANSACTION_ID" NUMBER(10,0) NOT NULL,
    "REQUEST_DATE" DATE DEFAULT SYSDATE NOT NULL,
    "REQUEST_STATUS_CODE" VARCHAR2(12 CHAR) DEFAULT PENDING NOT NULL,
    "QUEUE_MESSAGE_ID" VARCHAR2(64 CHAR),
    "TRANSACTION_SOURCE" VARCHAR2(12 CHAR) NOT NULL,
    "OFFENDER_BOOK_ID_1" NUMBER(10,0),
    "ROOT_OFFENDER_ID_1" NUMBER(10,0),
    "OFFENDER_ID_1" NUMBER(10,0),
    "OFFENDER_ID_DISPLAY_1" VARCHAR2(10 CHAR),
    "LAST_NAME_1" VARCHAR2(35 CHAR),
    "FIRST_NAME_1" VARCHAR2(35 CHAR),
    "OFFENDER_BOOK_ID_2" NUMBER(10,0),
    "ROOT_OFFENDER_ID_2" NUMBER(10,0),
    "OFFENDER_ID_2" NUMBER(10,0),
    "OFFENDER_ID_DISPLAY_2" VARCHAR2(10 CHAR),
    "LAST_NAME_2" VARCHAR2(35 CHAR),
    "FIRST_NAME_2" VARCHAR2(35 CHAR),
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
     CONSTRAINT "MERGE_TRANSACTIONS_PK" PRIMARY KEY ("MERGE_TRANSACTION_ID"),
  );

  CREATE UNIQUE INDEX "MERGE_TRANSACTIONS_PK" ON "MERGE_TRANSACTIONS" ("MERGE_TRANSACTION_ID");