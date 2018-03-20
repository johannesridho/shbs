INSERT INTO users VALUES (1, 'admin', '$2a$10$4yB3di.lJb3/Uh6edRwAlOHU/h31PrcEssd79oS0sNmIHDmUAw9Wu', '1');
INSERT INTO authorities VALUES (1, 'admin', 'ROLE_ADMIN');
INSERT INTO room_type VALUES (1, "executive", "executive room only for 1 person", "", 30, 1000, 1521566968000, 1521566968000);
INSERT INTO customer VALUES (1, 'customer', '$2a$10$4yB3di.lJb3/Uh6edRwAlOHU/h31PrcEssd79oS0sNmIHDmUAw9Wu', 'Customer', 1521566968000, 1521566968000);
INSERT INTO reservation VALUES (1, 1, 1, 5, 1553102665000, 1555781065000, FALSE, 1521566968000, 1521566968000);