SET
  foreign_key_checks = 0;

DROP TABLE IF EXISTS post CASCADE;

DROP TABLE IF EXISTS survey CASCADE;

DROP TABLE IF EXISTS user CASCADE;

DROP TABLE IF EXISTS hibernate_sequence CASCADE;

DROP TABLE IF EXISTS point_history CASCADE;

DROP TABLE IF EXISTS point_request CASCADE;

DROP TABLE IF EXISTS user_favorite CASCADE;

DROP TABLE IF EXISTS additional_favorite CASCADE;

DROP TABLE IF EXISTS attach CASCADE;

DROP TABLE IF EXISTS change_pwd_dto CASCADE;

DROP TABLE IF EXISTS point CASCADE;

DROP TABLE IF EXISTS additional CASCADE;

DROP TABLE IF EXISTS participate_survey CASCADE;

DROP TABLE IF EXISTS user_participate_survey CASCADE;

DROP TABLE IF EXISTS survey_variables CASCADE;

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
  id               BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  email            VARCHAR(255) NOT NULL,
  name             VARCHAR(255) NOT NULL,
  password         VARCHAR(255) NOT NULL,
  phone            VARCHAR(11)  NOT NULL,
  point            BIGINT       NULL DEFAULT 0,
  recommend        VARCHAR(11)  NULL,
  role             VARCHAR(255) NULL DEFAULT 'USER',
  birth            DATETIME(6)  NULL,
  birth_type       VARCHAR(255) NULL DEFAULT '양력',
  education        VARCHAR(255) NULL,
  gender           VARCHAR(20)  NULL DEFAULT 'MALE',
  industry         VARCHAR(255) NULL,
  registered_at    DATETIME(6)  NULL DEFAULT NOW(),
  job              VARCHAR(255) NULL,
  marry            BIT          NULL,
  is_email_receive BIT          NULL,
  is_sms_receive   BIT          NULL,
  UNIQUE (email)
);

CREATE TABLE user_favorite
(
  user_id  BIGINT       NOT NULL,
  favorite VARCHAR(255) NULL,
  FOREIGN KEY (user_id) REFERENCES user (id)
);


CREATE TABLE post
(

  pno            BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
  is_new         BIT           NULL     DEFAULT FALSE,
  registered_at  DATETIME(6)   NULL     DEFAULT NOW(),
  search_content VARCHAR(3000) NULL,
  title          VARCHAR(255)  NULL,
  view_content   VARCHAR(3000) NULL,
  view_count     BIGINT        NOT NULL DEFAULT 0,
  user_id        BIGINT        NOT NULL,
  type           VARCHAR(30)   NULL,
  FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE attach
(
  id            BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  file_path     VARCHAR(255) NULL,
  file_size     BIGINT       NULL,
  file_name     VARCHAR(255) NULL,
  original_name VARCHAR(255) NULL,
  pno           BIGINT       NULL,
  FOREIGN KEY (pno) REFERENCES post (pno)
);

CREATE TABLE survey
(
  id           BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title        VARCHAR(255) NOT NULL,
  survey_id    BIGINT       NOT NULL,
  created_at   DATETIME     NOT NULL,
  end_at       DATETIME     NULL,
  point        BIGINT       NOT NULL DEFAULT 0,
  href         VARCHAR(255) NOT NULL,
  progress     VARCHAR(255) NOT NULL DEFAULT 'BEFORE',
  minimum_time INT          NOT NULL,
  count        BIGINT       NOT NULL DEFAULT 0,
  UNIQUE (survey_id)
);

CREATE TABLE participate_survey
(
  id              BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id         BIGINT       NOT NULL,
  survey_id       BIGINT       NOT NULL,
  participated_at DATETIME     NOT NULL DEFAULT NOW(),
  survey_point    BIGINT       NOT NULL,
  hash            VARCHAR(255) NOT NULL,
  finished        BIT          NOT NULL DEFAULT FALSE,
  UNIQUE (hash),
  FOREIGN KEY (user_id) REFERENCES user (id),
  FOREIGN KEY (survey_id) REFERENCES survey (id)
);

CREATE TABLE user_participate_survey
(
  user_id               BIGINT NOT NULL,
  participate_survey_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE collector
(
  id             BIGINT       NOT NULL AUTO_INCREMENT,
  collector_id   BIGINT       NOT NULL UNIQUE,
  name           VARCHAR(255) NULL,
  href           VARCHAR(255) NULL,
  participateurl VARCHAR(500) NULL,
  survey_id      BIGINT,
  response_count BIGINT,
  FOREIGN KEY (survey_id) REFERENCES survey (id)
);

CREATE TABLE point_history
(
  id      BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  amount  BIGINT NOT NULL,
  sign    BIT    NOT NULL,
  total   BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE point_request
(
  id            BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  uid           BIGINT       NOT NULL,
  email         VARCHAR(255) NOT NULL,
  request_point BIGINT       NOT NULL DEFAULT 0,
  requested_at  DATETIME     NOT NULL DEFAULT NOW(),
  bank          VARCHAR(20)  NOT NULL,
  account       VARCHAR(50)  NOT NULL,
  progress      VARCHAR(20)  NOT NULL DEFAULT 'REQUESTED'
);

CREATE TABLE survey_variables
(
  survey_id BIGINT       NOT NULL,
  variable  VARCHAR(255) NULL,
  FOREIGN KEY (survey_id) REFERENCES survey (id)
);