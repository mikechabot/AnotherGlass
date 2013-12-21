create table wines (id serial unique, created_at timestamp, updated_at timestamp, wines_com_id bigint, name varchar(256), description varchar(5000), type varchar(128), vintage varchar(256), region_id bigint, appellation_id bigint, vineyard_id bigint);
create table regions (id serial unique, created_at timestamp, updated_at timestamp, wines_com_id bigint, name varchar(256), area varchar(256));
create table vineyards (id serial unique, created_at timestamp, updated_at timestamp, wines_com_id bigint, name varchar(256), image_url varchar(512), appellation_id bigint);
create table appellations (id serial unique, created_at timestamp, updated_at timestamp, wines_com_id bigint, name varchar(256), region_id bigint);
