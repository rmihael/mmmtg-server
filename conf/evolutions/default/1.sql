# --- !Ups

create table cards (
  id                        bigint not null primary key auto_increment,
  name                      varchar(255) not null
);

# --- !Downs

drop table if exists cards;