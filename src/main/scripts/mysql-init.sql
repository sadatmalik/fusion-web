DROP DATABASE IF EXISTS fusion_db;
DROP USER IF EXISTS `fusion_app`@`%`;
CREATE DATABASE IF NOT EXISTS fusion_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `fusion_app`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `fusion_db`.* TO `fusion_app`@`%`;
FLUSH PRIVILEGES;