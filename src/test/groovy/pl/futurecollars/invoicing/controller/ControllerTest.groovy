package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.TaxResult
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    JsonService jsonService

    static final String INVOICES_ENDPOINT = "/invoices"
    static final String TAXES_ENDPOINT = "/taxes"

    def cleanup() {
        deleteAllInvoices()
    }

    int addOneInvoice(Invoice invoice) {
        def invoiceAsJsonString = jsonService.objectToJsonString(invoice)

        String id = mockMvc.perform(post(INVOICES_ENDPOINT)
                .content(invoiceAsJsonString)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .getContentAsString()


        return id as int
    }

    List<Invoice> addInvoices(int howMany) {
        (1..howMany).collect({ id ->
            def invoice = TestHelpers.invoice(id)
            invoice.id = addOneInvoice(invoice)
            return invoice
        })
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get(INVOICES_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .getContentAsString()

        return jsonService.stringToObject(response, Invoice[])
    }

    Invoice getInvoiceById(int id) {
        def response = mockMvc.perform(get("$INVOICES_ENDPOINT/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .getContentAsString()
        return jsonService.stringToObject(response, Invoice)
    }

    void deleteInvoice(int id) {
        mockMvc.perform(delete("$INVOICES_ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

    void deleteAllInvoices() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    TaxResult calculateTax(Company company) {
        def response = mockMvc.perform(post("$TAXES_ENDPOINT")
                .content(jsonService.objectToJsonString(company))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return jsonService.stringToObject(response, TaxResult)
    }
}
