
  CREATE TABLE "OFFENDER_IND_SCH_SENTS"
   (    "EVENT_ID" NUMBER(10,0) NOT NULL,
    "OFFENDER_BOOK_ID" NUMBER(10,0) NOT NULL,
    "SENTENCE_SEQ" NUMBER(6,0) NOT NULL,
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
     CONSTRAINT "OFFENDE_IND_SCH_SENTS_PK" PRIMARY KEY ("EVENT_ID", "OFFENDER_BOOK_ID", "SENTENCE_SEQ"),
  );

  CREATE INDEX "OFFENDER_IND_SCH_SENTS_FK9" ON "OFFENDER_IND_SCH_SENTS" ("OFFENDER_BOOK_ID");


  CREATE UNIQUE INDEX "OFFENDE_IND_SCH_SENTS_PK" ON "OFFENDER_IND_SCH_SENTS" ("EVENT_ID", "OFFENDER_BOOK_ID", "SENTENCE_SEQ");


  CREATE INDEX "OFF_IND_SS_OFF_SENT_FK" ON "OFFENDER_IND_SCH_SENTS" ("OFFENDER_BOOK_ID", "SENTENCE_SEQ");
