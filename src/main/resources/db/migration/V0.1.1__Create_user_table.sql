CREATE TABLE user_roles
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(64)
);

INSERT INTO user_roles(id, title)
VALUES (1, 'ADMIN'),
       (2, 'EMPLOYEE');

CREATE TABLE user_account
(
    id        SERIAL PRIMARY KEY,
    firstname VARCHAR(64),
    lastname  VARCHAR(64),
    password  VARCHAR(64),
    role_id   INTEGER,
    CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id) references user_roles,
    CONSTRAINT UNIQUE_FIRSTNAME_CONSTRAINT UNIQUE (firstname),
    CONSTRAINT UNIQUE_LASTNAME_CONSTRAINT UNIQUE (lastname)
);

INSERT INTO user_account(firstname, lastname, password, role_id)
VALUES ('admin', '', 'admin', 1);