CREATE USER admin_schema WITH PASSWORD 'gfhfghtyureu7567tE';

CREATE SCHEMA car_metrics_rest AUTHORIZATION admin_schema;

GRANT ALL PRIVILEGES ON SCHEMA car_metrics_rest TO admin_schema;

-- даём доступ к самой базе (чтобы мог подключаться):
GRANT CONNECT ON DATABASE "car-metrics" TO admin_schema;

-- 4. Даём все права на СУЩЕСТВУЮЩИЕ таблицы и последовательности в схеме:
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA car_metrics_rest TO admin_schema;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA car_metrics_rest TO admin_schema;

-- 5. Настраиваем "default privileges", чтобы ВСЕ БУДУЩИЕ объекты
-- (таблицы, последовательности), которые кто-то создаст в этой схеме,
-- тоже были полностью доступны admin_schema:
ALTER DEFAULT PRIVILEGES IN SCHEMA car_metrics_rest
GRANT ALL PRIVILEGES ON TABLES TO admin_schema;

ALTER DEFAULT PRIVILEGES IN SCHEMA car_metrics_rest
GRANT ALL PRIVILEGES ON SEQUENCES TO admin_schema;

COMMIT;

CREATE TABLE IF NOT EXISTS car_metrics_rest.roles (
        id SERIAL PRIMARY KEY,
        name VARCHAR(50) NOT NULL,
        description VARCHAR(250)
    );
commit;

INSERT INTO car_metrics_rest.roles (name, description)
VALUES ('ROLE_ADMIN', 'admin'),
       ('ROLE_MODERATOR', 'moderator'),
       ('ROLE_USER', 'user');
commit;

