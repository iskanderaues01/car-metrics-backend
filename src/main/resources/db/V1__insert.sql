INSERT INTO roles(name, description) VALUES('ROLE_ADMIN', 'Управление пользователями, настройка прав доступа и обеспечение информационной безопасности');
INSERT INTO roles(name, description) VALUES('ROLE_SENIOR_TEACHER', 'Изменение учебных планов, контроль успеваемости студентов и выставление оценок');
INSERT INTO roles(name, description) VALUES('ROLE_TEACHER', 'Выставление оценок, ведение журнала успеваемости и участие в учебном процессе');
INSERT INTO roles(name, description) VALUES('ROLE_OPERATOR', 'Добавление и обновление личных данных студентов в системе');

insert into employees(id, created_date, date_of_birth, education, father_name, first_name, hire_date, last_name, status, phone)
VALUES (1,current_timestamp, current_timestamp, 'admin', 'admin', 'admin', current_timestamp, 'admin', 'ACTIVE', '87477730375');

insert into users_empl(id, empl_id,created, email, password, status, username)
VALUES (1,1,current_timestamp, 'admintester@gmail.com', '$2a$10$agJ.9ZXozk7pPs.Bwdzs4.97IGVBoBFT3JBIym8LnPaUNmWFdWjeG', 'ACTIVE', 'admin');
-- password 12345678


insert into user_roles(user_id, role_id)
values (1, 1);
