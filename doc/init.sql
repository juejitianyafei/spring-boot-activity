-- activity初始化语句
INSERT INTO `activity`.`act_id_user` (`ID_`, `REV_`, `FIRST_`, `LAST_`, `EMAIL_`, `PWD_`, `PICTURE_ID_`) VALUES ('admin', '1', '管理员', NULL, NULL, '000000', NULL);
INSERT INTO `activity`.`act_id_user` (`ID_`, `REV_`, `FIRST_`, `LAST_`, `EMAIL_`, `PWD_`, `PICTURE_ID_`) VALUES ('fanbingbing', '1', '范冰冰', NULL, NULL, '000000', NULL);
INSERT INTO `activity`.`act_id_user` (`ID_`, `REV_`, `FIRST_`, `LAST_`, `EMAIL_`, `PWD_`, `PICTURE_ID_`) VALUES ('fengxiaogang', '1', '冯小刚', NULL, NULL, '000000', NULL);
INSERT INTO `activity`.`act_id_user` (`ID_`, `REV_`, `FIRST_`, `LAST_`, `EMAIL_`, `PWD_`, `PICTURE_ID_`) VALUES ('wangzhonglei', '1', '王中磊', NULL, NULL, '000000', NULL);
INSERT INTO `activity`.`act_id_user` (`ID_`, `REV_`, `FIRST_`, `LAST_`, `EMAIL_`, `PWD_`, `PICTURE_ID_`) VALUES ('jingjiren', '1', '经纪人', NULL, NULL, '000000', NULL);


INSERT INTO `activity`.`act_id_group` (`ID_`, `REV_`, `NAME_`, `TYPE_`) VALUES ('deptLeader', '1', '部门领导', 'deptLeader');
INSERT INTO `activity`.`act_id_group` (`ID_`, `REV_`, `NAME_`, `TYPE_`) VALUES ('hr', '1', '人力', 'hr');
INSERT INTO `activity`.`act_id_group` (`ID_`, `REV_`, `NAME_`, `TYPE_`) VALUES ('inspector', '1', '总监', 'inspector');


INSERT INTO `activity`.`act_id_membership` (`USER_ID_`, `GROUP_ID_`) VALUES ('fengxiaogang', 'deptLeader');
INSERT INTO `activity`.`act_id_membership` (`USER_ID_`, `GROUP_ID_`) VALUES ('wangzhonglei', 'inspector');
INSERT INTO `activity`.`act_id_membership` (`USER_ID_`, `GROUP_ID_`) VALUES ('jingjiren', 'hr');
	