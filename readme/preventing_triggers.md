# **Preventing Trigger Execution**
### **Background**
Nomis has over 700 triggers, these triggers are responsible for executing PL/SQL blocks which can result in data manipulation and further events inside & outside of NOMIS.

For a more in-depth insight into NOMIS dependencies, please see [here](https://miro.com/app/board/o9J_lYdmgu0=/?fromRedirect=1).
### **What is A trigger in Oracle?**
Oracle allows you to define procedures that are implicitly executed when an INSERT, UPDATE, or DELETE statement is issued against the associated table. These procedures are called database triggers.

A trigger can include SQL and PL/SQL statements to execute as a unit and can invoke stored procedures. However, procedures and triggers differ in the way that they are invoked. While a procedure is explicitly executed by a user, application, or trigger, one or more triggers are implicitly fired (executed) by Oracle when a triggering INSERT, UPDATE, or DELETE statement is issued, no matter which user is connected or which application is being used.
### **NOMIS Trigger Example**


```
TRIGGER OMS_OWNER.OFFENDER_BOOKINGS_T4
AFTER INSERT OR UPDATE
ON OMS_OWNER.OFFENDER_BOOKINGS
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN
-- Lines added by ******** to check if the trigger code should be executed or not
   IF SYS_CONTEXT ('NOMIS', 'AUDIT_MODULE_NAME', 50) = 'MERGE'
   THEN
      RETURN;
   END IF;

```


### **Preventing Triggers when executing deletions**
Example script which disables triggers and enables them again.

```
v_saved_context := SYS_CONTEXT ('NOMIS', 'AUDIT_MODULE_NAME', 50);

nomis_context.set_context ('AUDIT_MODULE_NAME', 'MERGE');

Call to procedure, e.g.   delete_booking (p_offender_id_display => 'A?????', p_offender_book_id => ?????,  p_agency_incident_id       => NULL  -- pass if want to delete agency incident if only 1 offender );
   
   
   
   
   -- Restore context value for audit_module_name
   nomis_context.set_context('AUDIT_MODULE_NAME', v_saved_context);
```

As can be seen above, setting the context to MERGE prior to the execution of the procedure will prevent the triggers from being executed. This is because all triggers in NOMIS which are triggered by a DELETE statement in NOMIS have a defensive check of the SYS\_CONTEXT type. Currently, this application sets the context as NOMIS_DELETION_SERVICE. Please see the `DeceasedOffenderDeletionService.kt` class to see how it is done.

### **Triggers with no defensive check**
We have established what the types of triggers are that do no have defensive checks using the ‘MERGE’ context value.

They are as follows:

- Triggers on tables that don't hold offender related data.
- Audit data triggers, those that end in \_TA.
- OFFENDERS\_XTAG\_EVENTS (OFFENDERS)
- AGY\_PRISONER\_PAY\_PROFILES\_T1 (AGY\_PRISONER\_PAY\_PROFILES)
- OFF\_ALERTS\_API\_EVENT(OFFENDER\_ALERTS)

Considering the tables we intend to delete, the complete and base-record deletions can be executed whilst preventing trigger execution.
### **Key Terms Glossary**
#### **Oracle Sessions**
A logical entity in the database [**instance**](https://docs.oracle.com/cd/E28271_01/server.1111/e25789/glossary.htm#CBAFGFCJ) memory that represents the state of a current user login to a database. A single [**connection**](https://docs.oracle.com/cd/E28271_01/server.1111/e25789/glossary.htm#CBAFHDAD) can have 0, 1, or more sessions established on it.
#### **Context**
A set of application-defined attributes that validates and secures an application. The SQL statement CREATE CONTEXT creates namespaces for contexts.
