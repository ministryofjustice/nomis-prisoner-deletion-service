CREATE TABLE "OMS_ROLES"
(
  "ROLE_ID"                       NUMBER(10, 0)                       NOT NULL ,
  "ROLE_NAME"                     VARCHAR2(30 CHAR)                   NOT NULL ,
  "ROLE_SEQ"                      NUMBER(3, 0)                        NOT NULL ,
  "CREATE_DATETIME"               TIMESTAMP(9) DEFAULT systimestamp   NOT NULL ,
  "CREATE_USER_ID"                VARCHAR2(32 CHAR) DEFAULT USER      NOT NULL ,
  "MODIFY_DATETIME"               TIMESTAMP(9),
  "MODIFY_USER_ID"                VARCHAR2(32 CHAR),
  "ROLE_CODE"                     VARCHAR2(30 CHAR)                   NOT NULL ,
  "PARENT_ROLE_CODE"              VARCHAR2(30 CHAR),
  "AUDIT_TIMESTAMP"               TIMESTAMP(9),
  "AUDIT_USER_ID"                 VARCHAR2(32 CHAR),
  "AUDIT_MODULE_NAME"             VARCHAR2(65 CHAR),
  "AUDIT_CLIENT_USER_ID"          VARCHAR2(64 CHAR),
  "AUDIT_CLIENT_IP_ADDRESS"       VARCHAR2(39 CHAR),
  "AUDIT_CLIENT_WORKSTATION_NAME" VARCHAR2(64 CHAR),
  "AUDIT_ADDITIONAL_INFO"         VARCHAR2(256 CHAR),
  "ROLE_TYPE"                     VARCHAR2(12 CHAR),
  "ROLE_FUNCTION"                 VARCHAR2(12 CHAR) DEFAULT 'GENERAL' NOT NULL ,
  "SYSTEM_DATA_FLAG"              VARCHAR2(1 CHAR) DEFAULT 'N'        NOT NULL ,
  CONSTRAINT "OMS_ROLES_UK" UNIQUE ("ROLE_CODE"),
  CONSTRAINT "USER_GROUPS_PK" PRIMARY KEY ("ROLE_ID"),
  CONSTRAINT "OMS_ROLES_OMS_ROLES_FK" FOREIGN KEY ("PARENT_ROLE_CODE")
  REFERENCES "OMS_ROLES" ("ROLE_CODE")
);


COMMENT ON COLUMN "OMS_ROLES"."ROLE_ID" IS 'PK Of the role';

COMMENT ON COLUMN "OMS_ROLES"."ROLE_NAME" IS 'The description of the role';

COMMENT ON COLUMN "OMS_ROLES"."ROLE_SEQ" IS 'The listing order';

COMMENT ON COLUMN "OMS_ROLES"."CREATE_DATETIME" IS 'The timestamp when the record is created';

COMMENT ON COLUMN "OMS_ROLES"."CREATE_USER_ID" IS 'The user who creates the record';

COMMENT ON COLUMN "OMS_ROLES"."MODIFY_DATETIME" IS 'The timestamp when the record is modified ';

COMMENT ON COLUMN "OMS_ROLES"."MODIFY_USER_ID" IS 'The user who modifies the record';

COMMENT ON COLUMN "OMS_ROLES"."ROLE_CODE" IS 'The system role (as Oracle database role)';

COMMENT ON COLUMN "OMS_ROLES"."PARENT_ROLE_CODE" IS 'The parent of the system role (for grouping';

COMMENT ON TABLE "OMS_ROLES" IS 'The TAG application roles ';


CREATE INDEX "OMS_ROLES_NI1"
  ON "OMS_ROLES" ("PARENT_ROLE_CODE");


