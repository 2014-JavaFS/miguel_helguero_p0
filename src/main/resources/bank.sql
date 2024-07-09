drop table if exists USERS;
CREATE TABLE users(
	id serial primary key,
	email VARCHAR(64) not null unique,
	password VARCHAR(64) not NULL,
	balance NUMERIC(26, 2) not null check(balance >= 0.00)
);

insert into users(email, password, balance) values(
	'miguel@email.com',
	'miguelpw',
	20.24
);

insert into users(email, password, balance) values(
	'josh@gmail.com',
	'joshpw',
	68042.54
);

insert into users(email, password, balance) values(
	'javaboy@outlook.com',
	'javapw',
	1254832.98
);

insert into users(email, password, balance) values(
	'helguero@email.com',
	'helgueropw',
	0.00
);


select * from users;
