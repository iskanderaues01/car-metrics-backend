CREATE USER admin_schema WITH PASSWORD 'gfhfghtyureu7567tE';

CREATE SCHEMA car_metrics_rest AUTHORIZATION admin_schema;

GRANT ALL PRIVILEGES ON SCHEMA car_metrics_rest TO admin_schema;