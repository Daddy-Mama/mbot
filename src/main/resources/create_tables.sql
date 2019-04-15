create table if not exists questionnare
(
	id serial not null
		constraint questionnare_pk
			primary key,
	user_name varchar(255) not null,
	photo_id varchar(255),
	information varchar(1000) not null,
	enter_price integer not null
);

alter table questionnare owner to postgres;

create unique index if not exists questionnare_id_uindex
	on questionnare (id);

create unique index if not exists questionnare_user_name_uindex
	on questionnare (user_name);









create table if not exists "user"
(
	id integer not null
		constraint user_pk
			primary key,
	nickname varchar(255) not null,
	bought_meetings integer
		constraint user_questionnare_id_fk_2
			references questionnare,
	my_questionnare integer
		constraint user_questionnare_id_fk
			references questionnare
				on update cascade on delete cascade,
	pursuit integer not null,
	chat_id integer not null
);

alter table "user" owner to postgres;

create unique index if not exists user_id_uindex
	on "user" (id);

create unique index if not exists user_my_questionnare_uindex
	on "user" (my_questionnare);

create unique index if not exists user_nickname_uindex
	on "user" (nickname);






create table if not exists user_questionnare
(
	id serial not null
		constraint user_questionnare_pk
			primary key,
	user_id integer not null
		constraint fkkcoyc6sl1tpfe3shgti6neeyv
			references questionnare
		constraint user_questionnare_user_id_fk
			references "user"
				on update cascade on delete cascade,
	questionnare_id integer not null
		constraint user_questionnare_questionnare_id_fk
			references questionnare
);

alter table user_questionnare owner to postgres;

create unique index if not exists user_questionnare_id_uindex
	on user_questionnare (id);

