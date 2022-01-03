CREATE TABLE "OFFENDER_OIC_SANCTIONS"
(
  "OFFENDER_BOOK_ID"              NUMBER(10, 0)                     NOT NULL ,
  "SANCTION_SEQ"                  NUMBER(6, 0)                      NOT NULL ,
  "OIC_SANCTION_CODE"             VARCHAR2(12 CHAR),
  "COMPENSATION_AMOUNT"           NUMBER(11, 2),
  "SANCTION_MONTHS"               NUMBER(3, 0),
  "SANCTION_DAYS"                 NUMBER(3, 0),
  "COMMENT_TEXT"                  VARCHAR2(240 CHAR),
  "EFFECTIVE_DATE"                DATE                              NOT NULL ,
  "APPEALING_DATE"                DATE,
  "CONSECUTIVE_OFFENDER_BOOK_ID"  NUMBER(10, 0),
  "CONSECUTIVE_SANCTION_SEQ"      NUMBER(6, 0),
  "OIC_HEARING_ID"                NUMBER(10, 0),
  "STATUS"                        VARCHAR2(12 CHAR),
  "OFFENDER_ADJUST_ID"            NUMBER(10, 0),
  "RESULT_SEQ"                    NUMBER(6, 0),
  "CREATE_DATETIME"               TIMESTAMP(9) DEFAULT systimestamp NOT NULL ,
  "CREATE_USER_ID"                VARCHAR2(32 CHAR) DEFAULT USER    NOT NULL ,
  "MODIFY_DATETIME"               TIMESTAMP(9),
  "MODIFY_USER_ID"                VARCHAR2(32 CHAR),
  "STATUS_DATE"                   DATE,
  "AUDIT_TIMESTAMP"               TIMESTAMP(9),
  "AUDIT_USER_ID"                 VARCHAR2(32 CHAR),
  "AUDIT_MODULE_NAME"             VARCHAR2(65 CHAR),
  "AUDIT_CLIENT_USER_ID"          VARCHAR2(64 CHAR),
  "AUDIT_CLIENT_IP_ADDRESS"       VARCHAR2(39 CHAR),
  "AUDIT_CLIENT_WORKSTATION_NAME" VARCHAR2(64 CHAR),
  "AUDIT_ADDITIONAL_INFO"         VARCHAR2(256 CHAR),
  "OIC_INCIDENT_ID"               NUMBER(10, 0),
  "LIDS_SANCTION_NUMBER"          NUMBER(6, 0),

  CONSTRAINT "OFFENDER_OIC_SANCTIONS_PK" PRIMARY KEY ("OFFENDER_BOOK_ID", "SANCTION_SEQ"),

  CONSTRAINT "OIC_OS_OIC_HR_FK1"          FOREIGN KEY ("OIC_HEARING_ID", "RESULT_SEQ")                             REFERENCES "OIC_HEARING_RESULTS" ("OIC_HEARING_ID", "RESULT_SEQ"),
  CONSTRAINT "OFF_OS_OFF_OS_F1"           FOREIGN KEY ("CONSECUTIVE_OFFENDER_BOOK_ID", "CONSECUTIVE_SANCTION_SEQ") REFERENCES "OFFENDER_OIC_SANCTIONS" ("OFFENDER_BOOK_ID", "SANCTION_SEQ"),
  CONSTRAINT "OFF_OS_OFF_BKG_F1"          FOREIGN KEY ("OFFENDER_BOOK_ID")                                         REFERENCES "OFFENDER_BOOKINGS" ("OFFENDER_BOOK_ID"),
  CONSTRAINT "OFFENDER_OIC_SANCTIONS_FK9" FOREIGN KEY ("CONSECUTIVE_OFFENDER_BOOK_ID")                             REFERENCES "OFFENDER_BOOKINGS" ("OFFENDER_BOOK_ID")
);

COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."OFFENDER_BOOK_ID" IS 'Unique identifer for an offender booking.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."SANCTION_SEQ" IS 'Sequence number on sanction forming part of primary key.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."OIC_SANCTION_CODE" IS 'Reference Code ( OIC_SANCT )';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."COMPENSATION_AMOUNT" IS 'Penalty involving compensation amount.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."SANCTION_MONTHS" IS 'Penalty months imposed against sentences.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."SANCTION_DAYS" IS 'The number of penalty days imposed against sentences.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."COMMENT_TEXT" IS 'Pop-up edit window allowing penalty comments.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."EFFECTIVE_DATE" IS 'Effective date for penalty.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."APPEALING_DATE" IS 'Date of appeal.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."CONSECUTIVE_OFFENDER_BOOK_ID" IS 'FK to OIC sanction';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."CONSECUTIVE_SANCTION_SEQ" IS 'Specification of specific penalty that this may be consecutive to.';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."OIC_HEARING_ID" IS 'FK to OIC snaction';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."STATUS" IS 'Referece Code (OIC_SANCT_STS)';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."OFFENDER_ADJUST_ID" IS 'FK Offender OIC Appeal Penalty';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."RESULT_SEQ" IS 'Sequential number for hearing results';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."CREATE_DATETIME" IS 'The timestamp when the record is created';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."CREATE_USER_ID" IS 'The user who creates the record';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."MODIFY_DATETIME" IS 'The timestamp when the record is modified ';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."MODIFY_USER_ID" IS 'The user who modifies the record';
COMMENT ON COLUMN "OFFENDER_OIC_SANCTIONS"."STATUS_DATE" IS 'The date when the status changed';
COMMENT ON TABLE "OFFENDER_OIC_SANCTIONS" IS 'A punishment imposed upon an offender after having been found guilty of an offence defined in Prison Rule 51 or YOI Rule 55. The touchy-feely New-Labour term for punishment is now award..... A punishment can be made to run consecutively from other punishments imposed as a result of charges arising from unrelated incidents or from the same incident. NOTE : the primary key structure is not logical. It contains the Offender Booking Id of the guilty offender which, in fact, is inherited via parent entities.';


CREATE INDEX "OFFENDER_OIC_SANCTIONS_NI1"
  ON "OFFENDER_OIC_SANCTIONS" ("OIC_HEARING_ID", "RESULT_SEQ");
CREATE INDEX "OFF_OIC_SANCT_NI2"
  ON "OFFENDER_OIC_SANCTIONS" ("CONSECUTIVE_OFFENDER_BOOK_ID", "CONSECUTIVE_SANCTION_SEQ");


