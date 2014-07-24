create table user_facebooks (id serial unique, created_at timestamp, updated_at timestamp, user_id bigint, auth_code text, access_token text, expires_in bigint, constraint userfacebook_pkey primary key (id), constraint userfacebook_id_key unique (id), constraint userfacebook_user_id_key unique (user_id));
alter table users add column userfacebooks_id bigint;
