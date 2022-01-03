CREATE TABLE "PROFILE_CODES"
(
  "PROFILE_TYPE"                  VARCHAR2(12 CHAR)                 NOT NULL ,
  "PROFILE_CODE"                  VARCHAR2(12 CHAR)                 NOT NULL ,
  "DESCRIPTION"                   VARCHAR2(40 CHAR),
  "LIST_SEQ"                      NUMBER(6, 0)                      NOT NULL ,
  "UPDATE_ALLOWED_FLAG"           VARCHAR2(1 CHAR) DEFAULT 'Y'      NOT NULL ,
  "ACTIVE_FLAG"                   VARCHAR2(1 CHAR) DEFAULT 'Y'      NOT NULL ,
  "EXPIRY_DATE"                   DATE,
  "USER_ID"                       VARCHAR2(32 CHAR)                 NOT NULL ,
  "MODIFIED_DATE"                 DATE                              NOT NULL ,
  "CREATE_DATETIME"               TIMESTAMP(9) DEFAULT systimestamp NOT NULL ,
  "CREATE_USER_ID"                VARCHAR2(32 CHAR) DEFAULT USER    NOT NULL ,
  "MODIFY_DATETIME"               TIMESTAMP(9),
  "MODIFY_USER_ID"                VARCHAR2(32 CHAR),
  "AUDIT_TIMESTAMP"               TIMESTAMP(9),
  "AUDIT_USER_ID"                 VARCHAR2(32 CHAR),
  "AUDIT_MODULE_NAME"             VARCHAR2(65 CHAR),
  "AUDIT_CLIENT_USER_ID"          VARCHAR2(64 CHAR),
  "AUDIT_CLIENT_IP_ADDRESS"       VARCHAR2(39 CHAR),
  "AUDIT_CLIENT_WORKSTATION_NAME" VARCHAR2(64 CHAR),
  "AUDIT_ADDITIONAL_INFO"         VARCHAR2(256 CHAR),
  CONSTRAINT "PROFILE_CODES_PK" PRIMARY KEY ("PROFILE_TYPE", "PROFILE_CODE")
);


COMMENT ON COLUMN "PROFILE_CODES"."PROFILE_TYPE" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."PROFILE_CODE" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."DESCRIPTION" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."LIST_SEQ" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."UPDATE_ALLOWED_FLAG" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."ACTIVE_FLAG" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."EXPIRY_DATE" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."USER_ID" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."MODIFIED_DATE" IS ' - Column already exists';

COMMENT ON COLUMN "PROFILE_CODES"."CREATE_DATETIME" IS 'The timestamp when the record is created';

COMMENT ON COLUMN "PROFILE_CODES"."CREATE_USER_ID" IS 'The user who creates the record';

COMMENT ON COLUMN "PROFILE_CODES"."MODIFY_DATETIME" IS 'The timestamp when the record is modified ';

COMMENT ON COLUMN "PROFILE_CODES"."MODIFY_USER_ID" IS 'The user who modifies the record';

COMMENT ON TABLE "PROFILE_CODES" IS 'Profile Codes';


