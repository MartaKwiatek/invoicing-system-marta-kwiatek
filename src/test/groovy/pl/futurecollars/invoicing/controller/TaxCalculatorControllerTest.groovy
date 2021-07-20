package pl.futurecollars.invoicing.controller

import pl.futurecollars.invoicing.model.Car
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry

import static pl.futurecollars.invoicing.TestHelpers.company

class TaxCalculatorControllerTest extends ControllerTest {

    def "zeros are returned when no invoices were created"() {
        when:
        def response = calculateTax(company(0))

        then:
        response.income == 0
        response.costs == 0
        response.incomingVat == 0
        response.outgoingVat == 0
        response.earnings == 0
        response.vatToPay == 0
    }

    def "zeros are returned when taxIdNumber not exist in database"() {
        given:
        addInvoices(10)

        when:
        def response = calculateTax(company(-1))

        then:
        response.income == 0
        response.costs == 0
        response.incomingVat == 0
        response.outgoingVat == 0
        response.earnings == 0
        response.vatToPay == 0
    }

    def "correct values are returned when company is only seller"() {
        given:
        addInvoices(15)

        when:
        def response = calculateTax(company(9))

        then:
        response.income == 45000
        response.costs == 0
        response.incomingVat == 3600.0
        response.outgoingVat == 0
        response.earnings == 45000
        response.vatToPay == 3600.0
    }

    def "correct values are returned when company is only buyer"() {
        given:
        addInvoices(15)

        when:
        def response = calculateTax(company(16))

        then:
        response.income == 0
        response.costs == 21000
        response.incomingVat == 0
        response.outgoingVat == 1680.0
        response.earnings == -21000
        response.vatToPay == -1680.0
    }

    def "correct values are returned when company is buyer and seller"() {
        given:
        addInvoices(15)

        when:
        def response = calculateTax(company(11))

        then:
        response.income == 66000
        response.costs == 1000
        response.incomingVat == 5280.0
        response.outgoingVat == 80.0
        response.earnings == 65000
        response.vatToPay == 5200.0
    }

    def "correct values are returned when company uses car for the personal reasons"() {
        given:
        def invoice = Invoice.builder()
                .buyer(company(1))
                .seller(company(2))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(BigDecimal.valueOf(150))
                                .vatValue(BigDecimal.valueOf(34.51))
                                .carExpense(
                                        Car.builder()
                                                .isIncludingPrivateExpense(true)
                                                .build()
                                )
                                .build()
                ))
                .build()

        addOneInvoice(invoice)

        when:
        def response = calculateTax(invoice.getSeller())

        then:
        response.income == 150
        response.costs == 0
        response.earnings == 150
        response.incomingVat == 34.51
        response.outgoingVat == 0
        response.vatToPay == 34.51

        when:
        response = calculateTax(invoice.getBuyer())

        then:
        response.income == 0
        response.costs == 167.26
        response.earnings == -167.26
        response.incomingVat == 0
        response.outgoingVat == 17.25
        response.vatToPay == -17.25
    }

    def "correct values are returned in all calculations"() {
        given:
        def myCompany = Company.builder()
                .taxIdNumber("99999")
                .healthInsurance(319.94)
                .pensionInsurance(514.57)
                .build()

        def invoiceAsSeller = Invoice.builder()
                .buyer(company(1))
                .seller(myCompany)
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(76011.62)
                                .build()
                ))
                .build()

        def invoiceAsBuyer = Invoice.builder()
                .buyer(myCompany)
                .seller(company(2))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(11329.47)
                                .build()
                ))
                .build()

        addOneInvoice(invoiceAsSeller)
        addOneInvoice(invoiceAsBuyer)

        when:
        def response = calculateTax(myCompany)

        then:
        response.income == 76011.62
        response.costs == 11329.47
        response.earnings == 64682.15
        response.incomingVat == 0
        response.outgoingVat == 0
        response.vatToPay == 0
        response.healthInsurance == 275.50
        response.pensionInsurance == 514.57
        response.earningsMinusPensionInsurance == 64167.58
        response.taxCalculationBase == 64168
        response.incomeTax == 12191.92
        response.incomeTaxMinusHealthInsurance == 11916.42
        response.finalIncomeTax == 11916.00
    }
}
