CREATE TABLE EMPLOYEE_PERMISSIONS (
    EMPLOYEE_ID INTEGER NOT NULL,
    TASK_ID INTEGER NOT NULL,
    PRIMARY KEY (EMPLOYEE_ID, TASK_ID),
    CONSTRAINT EMPLOYEE_PERMISSIONS_ID_FK FOREIGN KEY (EMPLOYEE_ID) REFERENCES EMPLOYEES(ID),
    CONSTRAINT TASKS_PERMISSIONS_ID_FK FOREIGN KEY (TASK_ID) REFERENCES TASKS(id)
);