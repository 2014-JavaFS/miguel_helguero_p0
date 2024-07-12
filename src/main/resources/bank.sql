drop table if exists USERS;
CREATE TABLE users(
	id serial primary key,
	email VARCHAR(254) not null unique,
	password VARCHAR(64) not NULL,
	balance NUMERIC(26, 2) default 0.00 check(balance >= 0.00)
);

insert into users(email, password, balance)
values('miguel@email.com', 'miguelpassword', 20.24),
('helguero@email.com', 'helgueropassword', 2220.24),
('josh@gmail.com', 'joshpassword', 53535345.45),
('charles@revature.com', 'charlespassword', 2323.45),
('zoey@email.com', 'zoeypassword', 121232.21),
('bob@email.com', 'bobpassword', 00.01);

select * from users;


