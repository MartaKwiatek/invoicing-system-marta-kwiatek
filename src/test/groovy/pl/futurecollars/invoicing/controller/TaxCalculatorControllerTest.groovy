package pl.futurecollars.invoicing.controller

import static pl.futurecollars.invoicing.TestHelpers.company

class TaxCalculatorControllerTest extends ControllerTest{

    def "zeros are returned when no invoices were created"() {
//        when:
//        def response = calculateTax(company(0))
//
//        then:
//        response.income == 0
//        response.costs == 0
//        response.incomingVat == 0
//        response.outgoingVat == 0
//        response.earnings == 0
//        response.vatToPay == 0
    }

    def "zeros are returned when taxIdNumber not exist in database"() {
//        given:
//        addInvoices(10)
//
//        when:
//        def response = calculateTax(company(-1))
//
//        then:
//        response.income == 0
//        response.costs == 0
//        response.incomingVat == 0
//        response.outgoingVat == 0
//        response.earnings == 0
//        response.vatToPay == 0
    }

    def "correct values are returned when providing correct taxIdNumber"() {
//        given:
//        addInvoices(15)
//
//        when:
//        //company is only seller
//        def response = calculateTax(company(9))
//
//        then:
//        response.income == 45000
//        response.costs == 0
//        response.incomingVat == 3600.0
//        response.outgoingVat == 0
//        response.earnings == 45000
//        response.vatToPay == 3600.0
//
//        when:
//        //company is only buyer
//        response = calculateTax(company(16))
//
//        then:
//        response.income == 0
//        response.costs == 21000
//        response.incomingVat == 0
//        response.outgoingVat == 1680.0
//        response.earnings == -21000
//        response.vatToPay == -1680.0
//
//        when:
//        //company is buyer and seller
//        response = calculateTax(company(11))
//
//        then:
//        response.income == 66000
//        response.costs == 1000
//        response.incomingVat == 5280.0
//        response.outgoingVat == 80.0
//        response.earnings == 65000
//        response.vatToPay == 5200.0
    }
}
