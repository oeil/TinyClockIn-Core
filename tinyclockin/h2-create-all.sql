create table user (
  id                            integer auto_increment not null,
  email                         varchar(255) not null,
  token                         varchar(255) not null,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id)
);

create table action (
  id                            integer auto_increment not null,
  timestamp                     timestamp not null,
  type                          integer not null,
  description                   varchar(255) not null,
  workstation                   integer not null,
  userid                        integer,
  constraint pk_action primary key (id)
);

alter table action add constraint fk_action_userid foreign key (userid) references user (id) on delete restrict on update restrict;
create index ix_action_userid on action (userid);

