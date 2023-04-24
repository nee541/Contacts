CREATE DATABASE contacts DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
create table contacts.Contact (
	id bigint unsigned not null auto_increment,
    name text character set utf8mb4 collate utf8mb4_bin,
    phonenumber bigint unsigned not null,
    extrainfo json,
    
    primary key (id)
);

create user 'contactsApp'@'localhost' identified by '242017';
GRANT ALL ON javabase.* TO 'contactsApp'@'localhost';

