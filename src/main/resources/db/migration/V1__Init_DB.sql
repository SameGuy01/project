create table role (
    id bigserial,
    role varchar(255) not null,
    primary key (id)
);

create table users (
    id bigserial,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

create table users_roles (
    user_id int8 not null,
    roles_id int8 not null
);

 alter table if exists users_roles
     add constraint user_role_fk
     foreign key (roles_id) references role;

 alter table if exists users_roles
     add constraint role_user_fk
     foreign key (user_id) references users;