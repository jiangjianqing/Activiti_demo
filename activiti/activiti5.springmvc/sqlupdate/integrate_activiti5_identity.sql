/*1、删除原来的表格*/
DROP TABLE if exists ACT_ID_MEMBERSHIP;
DROP TABLE if exists ACT_ID_INFO;
DROP TABLE if exists ACT_ID_GROUP;
DROP TABLE if exists ACT_ID_USER;

/*2、创建试图代替*/

DROP VIEW if exists ACT_ID_USER;
CREATE VIEW ACT_ID_USER as select 
    Convert(COMMON_ID_USER.ID,char(64)) as ID_
    ,COMMON_ID_USER.FIRST_NAME as FIRST_
    ,COMMON_ID_USER.LAST_NAME as LAST_
    ,COMMON_ID_USER.EMAIL as EMAIL_
    ,COMMON_ID_USER.PASSWORD as PWD_
    ,0 as REV_
    ,Convert('',char(64)) as PICTURE_ID_
    from COMMON_ID_USER
;

DROP VIEW if exists ACT_ID_GROUP;
CREATE VIEW ACT_ID_GROUP as select 
    Convert(COMMON_ID_ROLE.ID,char(64)) as ID_
    ,0 as REV_
    ,COMMON_ID_ROLE.NAME as NAME_
    ,COMMON_ID_ROLE.TYPE as TYPE_
    from COMMON_ID_ROLE
;

DROP VIEW if exists ACT_ID_MEMBERSHIP;
CREATE VIEW ACT_ID_MEMBERSHIP as select 
    Convert(COMMON_ID_USER_ROLE.USER_ID,char(64)) as USER_ID_
    ,Convert(COMMON_ID_USER_ROLE.ROLE_ID,char(64)) as GROUP_ID_
    from COMMON_ID_USER_ROLE
;


DROP VIEW if exists ACT_ID_INFO;
CREATE VIEW ACT_ID_INFO as select 
    Convert(COMMON_ID_USER.ID,char(64)) as ID_
    ,0 as REV_
    ,Convert(COMMON_ID_USER.ID,char(64)) as USER_ID_
    ,Convert('',char(64)) as TYPE_
    ,Convert('',char(255)) as KEY_
    ,Convert('',char(255)) as VALUE_
    ,Convert('',binary) as PASSWORD_  /*原表中使用longblob*/
    ,Convert('',char(255)) as PARENT_ID_
    from COMMON_ID_USER
;

