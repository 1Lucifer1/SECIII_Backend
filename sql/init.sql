use `irbl`;
drop table if exists `user`;
create table `user`
(
    id        bigint auto_increment primary key,
    userId    bigint       not null
);