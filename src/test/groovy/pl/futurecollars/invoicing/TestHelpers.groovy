package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {
    static company(long id) {
        Company.builder()
        .taxIdNumber("$id")
        .address("ul. Bukowińska 24d/$id 02-703 Warszawa, Polska")
        .name("iCode Trust $id Sp. z o.o")
        .healthInsurance((BigDecimal.valueOf(id) * BigDecimal.valueOf(200)).setScale(2))
        .pensionInsurance((BigDecimal.valueOf(id) * BigDecimal.valueOf(50)).setScale(2))
        .build()
    }

    static product(long id) {
        InvoiceEntry.builder()
        .description("Programming course $id")
        .price((BigDecimal.valueOf(id * 1000)).setScale(2))
        .vatValue((BigDecimal.valueOf(id * 1000 * 0.08)).setScale(2))
        .vatRate(Vat.VAT_8)
        .build()
    }

    static invoice(long id) {
        Invoice.builder()
        .date(LocalDate.now())
        .number("9999/99/99/9999999/$id")
        .buyer(company(id + 10))
        .seller(company(id))
        .entries((1..id).collect({ product(it) }))
        .build()
    }
}
