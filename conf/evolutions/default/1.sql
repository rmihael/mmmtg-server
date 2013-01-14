# --- !Ups

create table blocks (
  id                        bigint not null primary key auto_increment,
  name                      varchar(255) not null
);

create table cards (
  id                        bigint not null primary key auto_increment,
  name                      varchar(255) not null,
  block_id                  varchar(128) not null,
  foreign key (block_id) references blocks(id) on delete restrict
);

# --- !Downs

drop table if exists cards;
drop table if exists blocks;