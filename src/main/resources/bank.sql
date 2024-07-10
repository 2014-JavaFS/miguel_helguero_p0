drop table if exists USERS;
CREATE TABLE users(
	id serial primary key,
	email VARCHAR(254) not null unique,
	password VARCHAR(64) not NULL,
	balance NUMERIC(26, 2) not null check(balance >= 0.00)
);

insert into users(email, password, balance)
values('miguel@email.com', 'miguelpassword', 20.24),
('helguero@email.com', 'helgueropassword', 2220.24),
('josh@gmail.com', 'joshpassword', 53535345.45),
('charles@revature.com', 'charlespassword', 2323.45),
('zoey@email.com', 'zoeypassword', 121232.21),
('bob@email.com', 'bobpassword', 00.01);

update users set balance = 00.00
where email like '%email.c__%';
select * from users;

create type member_enum as enum('ADMIN', 'PILOT', 'PASSENGER');

create table members(
	member_id serial primary key,
	first_name varchar(20),
	last_name varchar(40),
	email varchar(50),
	member_type member_enum default 'PASSENGER',
	password varchar(64)
);

-- establish relationship btwn flights and members(pilots)
alter table flights
add constraint fk_pilot_member_id foreign key(pilot) 
references members(member_id);


-- MEMBER STATEMENTS BELOW

insert into members
values(default, 'tommy', 'hoang', 'tommy@mail.com', default, 'pass123');

-- database admin adds pilot and an admin

