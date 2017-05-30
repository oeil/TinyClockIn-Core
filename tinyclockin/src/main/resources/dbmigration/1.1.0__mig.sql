-- apply changes
create table audit (
  id                            integer auto_increment not null,
  timestamp                     timestamp not null,
  email                         varchar(255),
  type                          varchar(255),
  url                           varchar(255),
  ip                            varchar(255),
  runtime                       bigint,
  status                        integer,
  constraint pk_audit primary key (id)
);