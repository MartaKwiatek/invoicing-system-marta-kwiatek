CREATE TABLE public.invoice_entry
(
    id              bigserial           NOT NULL,
    description     character varying(50),
    price           numeric(10, 2)      NOT NULL,
    vat_value       numeric(10, 2)      NOT NULL,
    vat_rate        bigint              NOT NULL,
    car_expense     bigint,
    PRIMARY KEY (id)
);

ALTER TABLE public.invoice_entry
    ADD CONSTRAINT vat_rate_fk FOREIGN KEY (vat_rate)
        REFERENCES public.vat (id);

ALTER TABLE public.invoice_entry
    ADD CONSTRAINT car_expense_fk FOREIGN KEY (car_expense)
        REFERENCES public.car (id) ON DELETE CASCADE;
