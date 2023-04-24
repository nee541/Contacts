use contacts;
show tables;
create table Contact (
	id bigint unsigned not null auto_increment,
    name text character set utf8mb4 collate utf8mb4_bin,
    phonenumber bigint unsigned not null,
    extrainfo json,
    
    primary key (id)
);

drop table Contact;

select * from contact;

delete from contact;

truncate Contact;

create table contactTest (
	id bigint unsigned not null auto_increment,
    name text character set utf8mb4 collate utf8mb4_bin,
    phonenumber bigint unsigned not null,
    extrainfo json,
    
    primary key (id)
);