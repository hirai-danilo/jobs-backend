--liquibase formatted sql
--changeset danilo-hirai:2021-10-31 author:danilo-hirai

CREATE SEQUENCE profissional_seq;
CREATE TABLE profissional
(
   id BIGINT default NEXTVAL('profissional_seq') NOT NULL,
   nome TEXT NOT NULL,
   cargo varchar(20) NOT NULL,
   nascimento date NOT NULL,
   data_criacao timestamp default now(),

   CONSTRAINT pk_profissional PRIMARY KEY (id)
);

--rollback drop table if exists profissional;
--rollback drop sequence if exists profissional_seq;