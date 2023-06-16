CREATE DATABASE LyricBot;

USE LyricBot;

CREATE TABLE Lyricer
(
    userId BIGINT UNIQUE NOT NULL PRIMARY KEY,
    link VARCHAR(512)
);

CREATE TABLE Request
(
    userId BIGINT UNIQUE NOT NULL PRIMARY KEY,
    name varchar(32) UNIQUE NOT NULL,
    link varchar(512) NOT NULL,
    title varchar(256) NOT NULL
);