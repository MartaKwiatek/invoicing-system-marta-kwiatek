package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.invoice

class InvoiceServiceUnitTest extends Specification{

    private InvoiceService service
    private Database database

    def setup() {
        database = Mock()
        service = new InvoiceService(database)
    }

    def "calling save() should delegate to database save() method"() {
        given:
        def invoice = invoice(1)

        when:
        service.save(invoice)

        then:
        1 * database.save(invoice)
    }

    def "calling delete() should delegate to database delete() method"() {
        given:
        def invoiceId = 123

        when:
        service.delete(invoiceId)

        then:
        1 * database.delete(invoiceId)
    }

    def "calling getById() should delegate to database getById() method"() {
        given:
        def invoiceId = 988

        when:
        service.getById(invoiceId)

        then:
        1 * database.getById(invoiceId)
    }

    def "calling getAll() should delegate to database getAll() method"() {
        when:
        service.getAll()

        then:
        1 * database.getAll()
    }

    def "calling update() should delegate to database update() method"() {
        given:
        def invoice = invoice(1)
        invoice.id = 1

        when:
        service.update(invoice.getId(), invoice)

        then:
        1 * database.update(invoice.getId(), invoice)
    }

    def "exception test when trying to save"() {
        setup:
        database.save(invoice(1)) >> {throw new RuntimeException("Saving invoice to database failed")}

        when:
        service.save(invoice(1))

        then:
        RuntimeException exception = thrown()
    }

    def "exception test when trying to getAll"() {
        setup:
        database.getAll() >> {throw new RuntimeException("Getting all invoices from database failed")}

        when:
        service.getAll()

        then:
        RuntimeException exception = thrown()
    }

//    def "exception test when trying to updating"() {
//        setup:
//        database.update(1, invoice(3)) >> {throw new RuntimeException("Updating invoice failed")}
//
//        when:
//        service.update(1, invoice(3))
//
//        then:
//        RuntimeException exception = thrown()
//    }

    def "exception test when trying to delete"() {
        setup:
        database.delete(1) >> {throw new RuntimeException("Deleting invoice failed")}

        when:
        service.delete(1)

        then:
        RuntimeException exception = thrown()
    }
}
