INSERT INTO AUTHORITY (id,role) VALUES (1, 'ADMIN');
INSERT INTO AUTHORITY (id,role) VALUES (2, 'USER');

INSERT INTO ACCOUNT (id,name,password,email,enabled) VALUES (1, 'admin', 'admin', 'admin@gmail.com', 1);
INSERT INTO ACCOUNT (id,name,password,email,enabled) VALUES (2, 'user', 'user', 'user@gmail.com', 1);

INSERT INTO USER_ROLES VALUES (1, 1);
INSERT INTO USER_ROLES VALUES (1, 2);
INSERT INTO USER_ROLES VALUES (2, 2);