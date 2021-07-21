CREATE TABLE public.companies
(
    id bigserial NOT NULL,
    address character varying(200) NOT NULL,
    name character varying(100) NOT NULL,
    health_insurance numeric(10, 2) NOT NULL DEFAULT 0,
    pension_insurance numeric(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE public.companies
    OWNER to postgres;