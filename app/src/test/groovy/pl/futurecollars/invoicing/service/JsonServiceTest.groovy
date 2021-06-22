package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelpers
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "can convert object to string and string to object"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.invoice(6)

        when:
        def invoiceAsString = jsonService.objectToString(invoice)

        and:
        def invoiceFromString = jsonService.stringToObject(invoiceAsString)

        then:
        invoice == invoiceFromString
    }

}
