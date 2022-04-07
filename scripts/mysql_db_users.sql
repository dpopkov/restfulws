DROP DATABASE IF EXISTS photo_app;
DROP USER IF EXISTS `photo_app_admin`@`localhost`;
CREATE DATABASE IF NOT EXISTS photo_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `photo_app_admin`@`localhost` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW, CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `photo_app`.* TO `photo_app_admin`@`localhost`;
FLUSH PRIVILEGES;

