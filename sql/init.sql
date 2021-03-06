DROP DATABASE IF EXISTS irbl;
CREATE DATABASE irbl DEFAULT CHARACTER SET utf8;
USE irbl;

DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`
(
    project_index       int(11) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    project_name        varchar(255) NOT NULL ,
    code_file_count     int(32) unsigned DEFAULT 0,
    report_count        int(32) unsigned DEFAULT 0
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `project` VALUES(1, 'test', 2, 1);
COMMIT;

DROP TABLE IF EXISTS `indicator`;
CREATE TABLE `indicator`
(
    project_index       int(11) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
    top_1               double default 0,
    top_5               double default 0,
    top_10              double default 0,
    mrr                 double default 0,
    map                 double default 0
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `indicator` VALUES (1, 0.5, 0.5, 0.5, 0.5, 0.5);
COMMIT;

DROP TABLE IF EXISTS `code_file`;
CREATE TABLE `code_file` (
                             file_index          int(32) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                             file_name           varchar(255) NOT NULL ,
                             project_index       int(11) unsigned NOT NULL ,
                             file_path           varchar(1023) NOT NULL ,
                             package_name       varchar(1023) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `code_file` VALUES (1, 'test1.java', 1, 'test/test1.java', 'test.test1.java'),
                               (2, 'test2.java', 1, 'test/test2.java', 'test.test2.java'),
                               (3, 'ACC.java', 1, 'swt-3.1/src/org/eclipse/swt/accessibility/ACC.java', 'ACC.java');

DROP TABLE IF EXISTS `project_word`;
CREATE TABLE `project_word` (
                                id                  int(32) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                                project_index       int(11) unsigned NOT NULL ,
                                word                varchar(64) NOT NULL ,
                                appear_files        int(21) unsigned DEFAULT 0,
                                idf                 double DEFAULT 0
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `project_word` VALUES (1, 1,'a', 2, 2), (2, 1, 'b', 2, 2);
COMMIT;

DROP TABLE IF EXISTS `file_word`;
CREATE TABLE `file_word` (
                             id                  int(32) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                             file_index          int(32) unsigned NOT NULL ,
                             word                varchar(64) NOT NULL ,
                             appear_times        int(32) unsigned DEFAULT 0,
                             tf                  double DEFAULT 0,
                             type                int(2) unsigned NOT NULL
)ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
BEGIN;
INSERT INTO `file_word` VALUES (1, 1, 'a', 10, 1, 0), (2, 2, 'b', 10, 1, 0), (3, 1, 'a', 10, 1, 1);
COMMIT;

DROP TABLE IF EXISTS `bug_report`;
CREATE TABLE `bug_report`(
    report_index        int(32) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    project_index       int(11) unsigned NOT NULL ,
    bug_id              int(32) unsigned NOT NULL ,
    open_date           varchar(32) NOT NULL ,
    fix_date            varchar(32),
    summary             varchar(255)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
BEGIN;
INSERT INTO `bug_report` VALUES (1, 1, 1000, '2020-11-12 08:40:00', '2020-12-12 08:40:00', 'test bug report');
COMMIT;

DROP TABLE IF EXISTS `fixed_file`;
CREATE TABLE `fixed_file`(
    report_index        int(32) unsigned NOT NULL ,
    file_index          int(32) unsigned NOT NULL ,
    file_identify       varchar (1023) NOT NULL ,
    file_package_name   varchar(1023) NOT NULL
)ENGINE=InnoDB;
ALTER TABLE `fixed_file` ADD PRIMARY KEY (report_index, file_index);
BEGIN;
INSERT INTO `fixed_file` VALUES ( 1, 1, 'test.test1.java','test.test1.java');
COMMIT;

DROP TABLE  IF EXISTS `rank_record`;
CREATE TABLE `rank_record`(
    report_index        int(32) unsigned NOT NULL ,
    file_index          int(32) unsigned NOT NULL ,
    file_rank           int(32) unsigned NOT NULL ,
    score               double  NOT NULL
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;
ALTER TABLE `rank_record` ADD PRIMARY KEY (report_index, file_index);
BEGIN;
INSERT INTO `rank_record` VALUES (1, 1, 2, 2.1), (1, 2, 1, 1.2);
COMMIT;