SET
foreign_key_checks = 0;

DROP TABLE if EXISTS polinsight.post CASCADE;

DROP TABLE if EXISTS polinsight.survey CASCADE;

DROP TABLE if EXISTS polinsight.user CASCADE;

DROP TABLE if EXISTS polinsight.hibernate_sequence CASCADE;

DROP TABLE if EXISTS polinsight.point_history CASCADE;

DROP TABLE if EXISTS polinsight.point_request CASCADE;

DROP TABLE if EXISTS polinsight.user_favorite CASCADE;

DROP TABLE if EXISTS polinsight.additional_favorite CASCADE;

DROP TABLE if EXISTS polinsight.attach CASCADE;

DROP TABLE if EXISTS polinsight.change_pwd_dto CASCADE;

DROP TABLE if EXISTS polinsight.point CASCADE;

DROP TABLE if EXISTS polinsight.additional CASCADE;

DROP TABLE if EXISTS polinsight.participate_survey CASCADE;

DROP TABLE if EXISTS polinsight.user_participate_survey CASCADE;

DROP TABLE if EXISTS polinsight.survey_variables CASCADE;

SET
foreign_key_checks = 1;

CREATE TABLE change_pwd_dto
(
  email VARCHAR(255) NOT NULL PRIMARY KEY,
  hash  VARCHAR(255) NOT NULL,
  name  VARCHAR(255) NOT NULL,
  UNIQUE (hash),
  UNIQUE (email)
);



CREATE TABLE user
(
  id               bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email            VARCHAR(255) NOT NULL,
  name             VARCHAR(255) NOT NULL,
  password         VARCHAR(255) NOT NULL,
  phone            VARCHAR(11)  NOT NULL,
  point            bigint NULL DEFAULT 0,
  recommend        VARCHAR(11) NULL,
  role             VARCHAR(255) NULL DEFAULT 'USER',
  birth            datetime(6) NULL,
  birth_type       VARCHAR(255) NULL DEFAULT '양력',
  education        VARCHAR(255) NULL,
  gender           VARCHAR(20) NULL DEFAULT 'MALE',
  industry         VARCHAR(255) NULL,
  registered_at    datetime(6) NULL DEFAULT now(),
  job              VARCHAR(255) NULL,
  marry            BIT NULL,
  is_email_receive BIT NULL,
  is_sms_receive   BIT NULL,
  UNIQUE (email)
);

CREATE TABLE user_favorite
(
  user_id  bigint NOT NULL,
  favorite VARCHAR(255) NULL,
  FOREIGN KEY (user_id) REFERENCES user (id)
);


CREATE TABLE post
(

  pno            bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
  is_new         BIT NULL DEFAULT FALSE,
  registered_at  datetime(6) NULL DEFAULT NOW(),
  search_content VARCHAR(2550) NULL,
  title          VARCHAR(255) NULL,
  view_content   VARCHAR(2550) NULL,
  view_count     bigint NOT NULL DEFAULT 0,
  user_id        bigint NOT NULL,
  type           VARCHAR(30) NULL,
  FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE attach
(
  id            bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
  file_path     VARCHAR(255) NULL,
  file_size     bigint NULL,
  file_name     VARCHAR(255) NULL,
  original_name VARCHAR(255) NULL,
  pno           bigint NULL,
  FOREIGN KEY (pno) REFERENCES post (pno)
);

CREATE TABLE survey
(
  id           bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title        VARCHAR(255) NOT NULL,
  survey_id    bigint       NOT NULL,
  created_at   datetime     NOT NULL,
  end_at       datetime NULL,
  point        bigint       NOT NULL DEFAULT 0,
  href         VARCHAR(255) NOT NULL,
  progress     VARCHAR(255) NOT NULL DEFAULT 'BEFORE',
  minimum_time INT          NOT NULL,
  count        bigint       NOT NULL DEFAULT 0,
  UNIQUE (survey_id)
);

CREATE TABLE participate_survey
(
  id              bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id         bigint       NOT NULL,
  survey_id       bigint       NOT NULL,
  participated_at datetime     NOT NULL DEFAULT now(),
  survey_point    bigint       NOT NULL,
  hash            VARCHAR(255) NOT NULL,
  finished        BIT          NOT NULL DEFAULT FALSE,
  UNIQUE (hash),
  FOREIGN KEY (user_id) REFERENCES user (id),
  FOREIGN KEY (survey_id) REFERENCES survey (id)
);

CREATE TABLE user_participate_survey
(
  user_id               bigint NOT NULL,
  participate_survey_id bigint NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE point_history
(
  id      bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id bigint NOT NULL,
  amount  bigint NOT NULL,
  sign    BIT    NOT NULL,
  total   bigint NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE point_request
(
  id            bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  uid           bigint       NOT NULL,
  email         VARCHAR(255) NOT NULL,
  request_point bigint       NOT NULL DEFAULT 0,
  requested_at  datetime     NOT NULL DEFAULT now(),
  bank          VARCHAR(20)  NOT NULL,
  account       VARCHAR(50)  NOT NULL,
  progress      VARCHAR(20)  NOT NULL DEFAULT 'REQUESTED'
);

CREATE TABLE survey_variables
(
  survey_id bigint NOT NULL,
  variable  VARCHAR(255) NULL,
  FOREIGN KEY (survey_id) REFERENCES survey (id)
);