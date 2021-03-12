DROP DATABASE IF EXISTS irbl;
CREATE DATABASE irbl DEFAULT CHARACTER SET utf8;
USE irbl;

DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`
(
    project_index       int(11) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    project_name        varchar(255) NOT NULL ,
    code_file_count     int(32) unsigned DEFAULT 0
)ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `project` VALUES(1, 'test', 2);
COMMIT;

DROP TABLE IF EXISTS `code_file`;
CREATE TABLE `code_file` (
                             file_index          int(21) unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                             file_name           varchar(255) NOT NULL ,
                             project_index       int(11) unsigned NOT NULL ,
                             file_path           varchar(1023) NOT NULL ,
                             word_count          int(32) unsigned DEFAULT 0
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `code_file` VALUES (1, 'test1.java', 1, 'test/test1.java', 10),
                               (2, 'test2.java', 1, 'test/test2.java', 10);

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
                             file_index          int(21) unsigned NOT NULL ,
                             word                varchar(64) NOT NULL ,
                             appear_times        int(32) unsigned DEFAULT 0,
                             tf                  double DEFAULT 0
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
BEGIN;
INSERT INTO `file_word` VALUES (1, 1, 'a', 10, 1), (2, 2, 'b', 10, 1);
COMMIT;