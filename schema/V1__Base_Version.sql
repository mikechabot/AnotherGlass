create table wines (ag_id serial unique, wine_id bigint, name varchar(256), description varchar(5000), retail varchar(128), type varchar(128), url varchar(512), vintage varchar(256), price_max numeric, price_min numeric, price_retail numeric, region_id bigint, appellation_id bigint, vineyard_id bigint);
create table regions (ag_id serial unique, region_id bigint unique, name varchar(256), url varchar(512), area varchar(256));
create table vineyards (ag_id serial unique, vineyard_id bigint unique, name varchar(256), url varchar(512), imageUrl varchar(512), appellation_id bigint);
create table appellations (ag_id serial unique, appellation_id bigint unique, name varchar(256), url varchar(512), region_id bigint);
