--liquibase formatted sql
--changeset danilo-hirai:2021-10-31_02 author:danilo-hirai

CREATE SEQUENCE contato_seq;
CREATE TABLE contato
(
   id BIGINT default NEXTVAL('contato_seq') NOT NULL,
   nome TEXT NOT NULL,
   contato TEXT NOT NULL,
   id_profissional BIGINT NOT NULL,

   CONSTRAINT fk_contato_profissional
                 FOREIGN KEY (id_profissional)
                 REFERENCES profissional(id)
                 MATCH SIMPLE,

   CONSTRAINT pk_contato PRIMARY KEY (id)
);

--rollback drop table if exists contato;
--rollback drop sequence if exists contato_seq;