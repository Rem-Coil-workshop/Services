CREATE TABLE tasks
(
    id   SERIAL primary key,
    name VARCHAR(64) not null,
    CONSTRAINT UNIQUE_TASK_NAME_CONSTRAINT UNIQUE (name)
);

CREATE TABLE employees
(
    id              SERIAL primary key,
    employee_number INTEGER     not null,
    name            VARCHAR(20) not null,
    surname         VARCHAR(20) not null,
    CONSTRAINT UNIQUE_EMPLOYEE_NAME_CONSTRAINT UNIQUE (employee_number)
);

CREATE TABLE boxes
(
    id         SERIAL primary key,
    box_number INTEGER not null,
    task_id    INTEGER,
    CONSTRAINT UNIQUE_BOX_NUMBER_CONSTRAINT UNIQUE (box_number),
    CONSTRAINT FK_BOXES_TASK FOREIGN KEY (task_id) references tasks
);

CREATE TABLE logs
(
    id          SERIAL primary key,
    employee_id INTEGER   not null,
    date        TIMESTAMP not null,
    task_id     INTEGER   not null,
    CONSTRAINT FK_LOGS_EMPLOYEE FOREIGN KEY (employee_id) references employees,
    CONSTRAINT FK_LOGS_TASK FOREIGN KEY (task_id) references tasks
);