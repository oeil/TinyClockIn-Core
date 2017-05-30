alter table action drop constraint if exists fk_action_user_id;
drop index if exists ix_action_user_id;

drop table if exists user;

drop table if exists action;

drop table if exists audit;

