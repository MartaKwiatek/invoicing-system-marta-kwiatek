CREATE TABLE public.invoice
(
    id bigserial NOT NULL,
    issue_date date NOT NULL,
    "number" character varying NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unique_invoice_number UNIQUE ("number")
);