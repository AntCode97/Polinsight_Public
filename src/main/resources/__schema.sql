drop table if exists additional_favorite cascade;

drop table if exists attach cascade;

drop table if exists board cascade;

drop table if exists change_pwd_dto cascade;

drop table if exists hibernate_sequence cascade;

drop table if exists point cascade;

drop table if exists user cascade;

drop table if exists additional cascade;

SET FOREIGN_KEY_CHECKS = 0;

create table change_pwd_dto
(
    email varchar(255) not null primary key,
    hash  varchar(255) null,
    name  varchar(255) null
);

create table point
(
    id          bigint       not null primary key,
    email       varchar(255) null,
    hash        varchar(255) null,
    point_value bigint       null,
    survey_id   bigint       null,
    uid         bigint       null
);

create table additional
(
    id         bigint auto_increment primary key,
    birth      datetime(6)  null,
    birth_type varchar(255) null,
    education  varchar(255) null,
    gender     int          null,
    industry   varchar(255) null,
    job        varchar(255) null,
    marry      bit          null
);

create table additional_favorite
(
    additional_id bigint       not null,
    favorite      varchar(255) null,
    foreign key (additional_id) references additional (id)
);

create table user
(
    id            bigint auto_increment primary key,
    email         varchar(255) null,
    name          varchar(255) null,
    password      varchar(255) null,
    phone         varchar(11)  null,
    point         bigint       null,
    recommend     varchar(11)  null,
    role          varchar(255) null,
    additional_id bigint       null,
    unique (email),
    foreign key (additional_id) references additional (id)
);

create table board
(
    bno           bigint        not null primary key,
    board_type    int           null,
    new_board     bit           null,
    registered_at datetime(6)   null,
    searchcontent varchar(2550) null,
    title         varchar(255)  null,
    viewcontent   varchar(2550) null,
    user_id       bigint        not null,
    foreign key (user_id) references user (id)
);

create table attach
(
    id            bigint       not null primary key,
    file_path     varchar(255) null,
    file_size     bigint       null,
    filename      varchar(255) null,
    original_name varchar(255) null,
    bno           bigint       null,
    foreign key (bno) references board (bno)
)