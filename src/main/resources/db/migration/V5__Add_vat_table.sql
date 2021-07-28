CREATE TABLE public.vat
(
    id bigserial NOT NULL,
    name character varying(20),
    rate numeric(3,2) NOT NULL,
    PRIMARY KEY (id)
);

insert into public.vat (name, rate)
values ('0', 0.00),
       ('5', 0.05),
       ('7', 0.07),
       ('8', 0.08),
       ('23', 0.23),
       ('ZW', 0.00);
