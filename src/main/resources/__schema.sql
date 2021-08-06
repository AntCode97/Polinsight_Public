DROP TABLE IF EXISTS additional_favorite CASCADE;

DROP TABLE IF EXISTS attach CASCADE;

DROP TABLE IF EXISTS board CASCADE;

DROP TABLE IF EXISTS change_pwd_dto CASCADE;

DROP TABLE IF EXISTS hibernate_sequence CASCADE;

DROP TABLE IF EXISTS point CASCADE;

DROP TABLE IF EXISTS user CASCADE;

DROP TABLE IF EXISTS additional CASCADE;

CREATE TABLE change_pwd_dto
(
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    hash  VARCHAR(255) NOT NULL,
    name  VARCHAR(255) NOT NULL,
    UNIQUE (hash),
    UNIQUE (email)
);

CREATE TABLE user_favorite
(
    user_id  BIGINT       NOT NULL,
    favorite VARCHAR(255) NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE user
(
    id               BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email            VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NULL,
    password         VARCHAR(255) NOT NULL,
    phone            VARCHAR(11)  NULL,
    point            BIGINT       NULL DEFAULT 0,
    recommend        VARCHAR(11)  NULL,
    role             VARCHAR(255) NULL DEFAULT 'USER',
    birth            DATETIME(6)  NULL,
    birth_type       VARCHAR(255) NULL DEFAULT '양력',
    education        VARCHAR(255) NULL,
    gender           INT          NULL,
    industry         VARCHAR(255) NULL,
    job              VARCHAR(255) NULL,
    marry            BIT          NULL,
    is_sms_receive   BIT          NULL,
    is_email_receive BIT          NULL,
    UNIQUE (email)
);

CREATE TABLE board
(
    bno           BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    board_type    INT           NULL,
    new_board     BIT           NULL,
    registered_at DATETIME(6)   NULL,
    searchcontent VARCHAR(2550) NULL,
    title         VARCHAR(255)  NULL,
    viewcontent   VARCHAR(2550) NULL,
    user_id       BIGINT        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE attach
(
    id            BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    file_path     VARCHAR(255) NULL,
    file_size     BIGINT       NULL,
    filename      VARCHAR(255) NULL,
    original_name VARCHAR(255) NULL,
    bno           BIGINT       NULL,
    FOREIGN KEY (bno) REFERENCES board (bno)
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
    minimum_time DATETIME     NOT NULL,
    count        INTEGER      NOT NULL DEFAULT 0,
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
)