drop table if exists users CASCADE;
drop table if exists accounts;

CREATE TABLE users(
	user_id serial primary key,
	email VARCHAR(254) not null unique,
	password VARCHAR(64) not NULL
);

CREATE table accounts(
	account_id SERIAL primary key,
	account_type VARCHAR(10) not null,
	balance NUMERIC(26, 2) default 0.00 check(balance >= 0.00),
	user_id INT not NULL
);

ALTER SEQUENCE accounts_account_id_seq RESTART WITH 111222001;

alter table accounts
add constraint fk__user_id foreign key(user_id)
references users(user_id);

insert into users(email, password)
values('miguel@email.com', 'miguelpassword'),
('helguero@email.com', 'helgueropassword'),
('josh@gmail.com', 'joshpassword'),
('charles@revature.com', 'charlespassword'),
('zoey@email.com', 'zoeypassword'),
('bob@email.com', 'bobpassword'),
('jose@yahoo.com', 'josepassword');

insert into accounts(account_type, balance, user_id)
values
('checkings', 350.42, 1),
('savings', 1050.42, 1),
('checkings', 1350.42, 2),
('savings', 3259.22, 2),
('checkings', 13439.18, 3),
('checkings', 25000.18, 4),
('savings', 1000000.01, 4),
('savings', 237482.18, 5),
('investment', 50000.00, 5);




--update accounts
--set balance = balance+50
--where user_id = 3 and account_type = 'savings';
select * from users;
--select * from accounts;

--select u.*, a.account_id, a.account_type, a.balance from users u
--join accounts a on u.user_id = a.user_id
--order by u.user_id;


