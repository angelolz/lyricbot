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
    name    varchar(32) UNIQUE NOT NULL,
    link    varchar(512)       NOT NULL,
    title   varchar(256)       NOT NULL
);

CREATE TABLE Winner
(
    user_id BIGINT UNIQUE      NOT NULL PRIMARY KEY,
    name    varchar(32) UNIQUE NOT NULL,
    title   varchar(512)       NOT NULL
);

CREATE TABLE Status
(
    time bigint,
    open tinyint default 1
);

INSERT INTO STATUS (time)
VALUES (-11);