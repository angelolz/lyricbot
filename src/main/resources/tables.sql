CREATE
    DATABASE LyricBot;
USE
    LyricBot;

CREATE TABLE Lyricer
(
    user_id BIGINT UNIQUE NOT NULL PRIMARY KEY,
    link    VARCHAR(512),
    banned  BOOLEAN default 0
);

CREATE TABLE Request
(
    user_id BIGINT UNIQUE      NOT NULL PRIMARY KEY,
    link    varchar(512)       NOT NULL,
    title   varchar(256)       NOT NULL
);

CREATE TABLE Winner
(
    winner_id int NOT NULL auto_increment PRIMARY KEY,
    user_id BIGINT UNIQUE      NOT NULL,
    title   varchar(512)       NOT NULL,
    added   timestamp          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    season int NOT NULL
);

CREATE TABLE Status
(
    time bigint,
    open tinyint default 1
);