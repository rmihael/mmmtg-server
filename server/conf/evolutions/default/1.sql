# --- !Ups

create table blocks (
  id                        bigint not null primary key auto_increment,
  name                      varchar(255) not null,
  shortname                 varchar(3) not null
);

create table cards (
  id                        bigint not null primary key auto_increment,
  name                      varchar(255) not null,
  block_id                  varchar(128) not null,
  foreign key (block_id) references blocks(id) on delete restrict,
  unique(name, block_id)
);

create table prices (
  id                        bigint not null primary key auto_increment,
  card_id                   bigint not null,
  dt                        bigint not null,
  price                     double not null,
  foreign key (card_id) references cards(id) on delete cascade
);

create table pricesources (
  id                        bigint not null primary key auto_increment,
  card_id                   bigint not null,
  type                      varchar(128) not null,
  url                       varchar(1024) not null,
  foreign key (card_id) references cards(id) on delete cascade
);

# --- !Downs

drop table if exists cards;
drop table if exists blocks;
drop table if exists prices;
drop table if exists pricesources;