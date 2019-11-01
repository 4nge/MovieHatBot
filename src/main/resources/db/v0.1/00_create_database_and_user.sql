CREATE DATABASE moviehatbot;

GRANT ALL ON moviehatbot.* TO 'admin' IDENTIFIED BY '{admin-pass}';
GRANT ALL ON moviehatbot.* TO 'movie-hat-bot' IDENTIFIED BY '{bot-pass}';

ALTER DATABASE moviehatbot CHARSET=utf8;