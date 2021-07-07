package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "can convert object to string and string to object"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.invoice(6)

        when:
        def invoiceAsString = jsonService.objectToJsonString(invoice)

        and:
        def invoiceFromString = jsonService.stringToObject(invoiceAsString, Invoice)

        then:
        invoice == invoiceFromString
    }

}
