--liquibase formatted sql
--changeset danilo-hirai:2021-10-31_03 author:danilo-hirai

INSERT INTO profissional VALUES (NEXTVAL('profissional_seq') , 'Paulo', 'DESENVOLVEDOR', '1990-01-12', now());
INSERT INTO profissional VALUES (NEXTVAL('profissional_seq') , 'Helen', 'DESIGNER', '2003-09-20', now());
INSERT INTO profissional VALUES (NEXTVAL('profissional_seq') , 'Karol', 'TESTER', '1993-05-27', now());
INSERT INTO profissional VALUES (NEXTVAL('profissional_seq') , 'Everton', 'SUPORTE', '1995-10-21', now());

--rollback delete from profissional;