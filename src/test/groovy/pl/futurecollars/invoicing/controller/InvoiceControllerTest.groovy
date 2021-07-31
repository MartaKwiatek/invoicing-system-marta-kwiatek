package pl.futurecollars.invoicing.controller

import org.springframework.http.MediaType
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Shared

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice

class InvoiceControllerTest extends ControllerTest {

    @Shared
    private boolean isSetupDone = false

    def setup() {
        if(!isSetupDone) {
            deleteAllInvoices()
            isSetupDone = true
        }
    }

    def "empty array is returned when no invoices were created"() {
        expect:
        getAllInvoices() == []
    }

    def "one invoice is successfully added to the database"() {
        given:
        Invoice invoiceToAdd = invoice(8)

        when:
        def id = addOneInvoice(invoiceToAdd)
        invoiceToAdd.id = id

        then:
        getAllInvoices().size() > 0
        getAllInvoices().get(0) == invoiceToAdd
    }

    def "more than one invoice is successfully added to the database"() {
        when:
        List<Invoice> invoicesToAdd = addInvoices(10)

        then:
        invoicesToAdd.size() == getAllInvoices().size()
//        invoicesToAdd == getAllInvoices()
    }

    def "returns correct ids when invoices added"() {
        given:
        def invoiceToAdd = invoice(1)

        expect:
        def id = addOneInvoice(invoiceToAdd)
        addOneInvoice(invoiceToAdd) == id + 1
        addOneInvoice(invoiceToAdd) == id + 2
        addOneInvoice(invoiceToAdd) == id + 3
        addOneInvoice(invoiceToAdd) == id + 4
        addOneInvoice(invoiceToAdd) == id + 5
    }

    def "getting existing invoice by id returns appropriate invoice"() {
        when:
        List<Invoice> invoicesToAdd = addInvoices(10)
        Invoice invoiceToFind = invoicesToAdd.get(4)

        then:
        getInvoiceById(invoiceToFind.getId()) == invoiceToFind
    }

    def "returns 404 not found status when trying to get invoice by non-existent id [#id]"() {
        given:
        addInvoices(10)

        expect:
        mockMvc.perform(get("$INVOICES_ENDPOINT/$id"))
                .andExpect(status().isNotFound())

        where:
        id << [-76, -1, 0, 57, 79]
    }

    def "one invoice is successfully deleted from the database"() {
        given:
        List<Invoice> invoicesToAdd = addInvoices(200)
        Invoice invoiceTODelete = invoicesToAdd.get(0)
        getAllInvoices().size() == 200

        when:
        deleteInvoice(invoiceTODelete.getId())

        then:
        getAllInvoices().size() == 199
    }

    def "all invoices are successfully deleted from the database"() {
        given:
        addInvoices(200)
        getAllInvoices().size() == 200

        when:
        deleteAllInvoices()

        then:
        getAllInvoices().size() == 0
    }

    def "returns 404 not found status when trying to delete invoice by non-existent id [#id]"() {
        given:
        addInvoices(10)

        expect:
        mockMvc.perform(delete("$INVOICES_ENDPOINT/$id"))
                .andExpect(status().isNotFound())

        where:
        id << [-76, -1, 0, 507, 529]
    }

    def "invoice is successfully updated"() {
        given:
        def id = addOneInvoice(invoice(1))
        def updatedInvoice = invoice(69)
        updatedInvoice.id = id
        def updatedInvoiceAsJson = jsonService.objectToJsonString(updatedInvoice)

        expect:
        mockMvc.perform(put("$INVOICES_ENDPOINT/$id")
                .content(updatedInvoiceAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())

        getInvoiceById(id) == updatedInvoice
    }

    def "returns 404 not found status when trying to update invoice by non-existent id [#id]"() {
        given:
        addInvoices(10)
        def updatedInvoice = jsonService.objectToJsonString(invoice(9))

        expect:
        mockMvc.perform(put("$INVOICES_ENDPOINT/$id")
                .content(updatedInvoice)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())

        where:
        id << [-76, -1, 0, 5000]
    }
}
