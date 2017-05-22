alter table action drop constraint if exists fk_action_userid;
drop index if exists ix_action_userid;

drop table if exists user;

drop table if exists action;

