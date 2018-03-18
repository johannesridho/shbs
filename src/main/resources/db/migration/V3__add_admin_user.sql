INSERT INTO users (username, password, enabled, created_at, updated_at) VALUES ('admin', '$2a$10$4yB3di.lJb3/Uh6edRwAlOHU/h31PrcEssd79oS0sNmIHDmUAw9Wu', '1', NOW(), NOW());
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
