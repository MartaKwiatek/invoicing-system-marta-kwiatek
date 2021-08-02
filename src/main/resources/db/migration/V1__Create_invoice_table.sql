CREATE TABLE public.invoice
(
    id      bigserial           NOT NULL,
    date    date                NOT NULL,
    number  character varying   NOT NULL,
    PRIMARY KEY (id)
    );
