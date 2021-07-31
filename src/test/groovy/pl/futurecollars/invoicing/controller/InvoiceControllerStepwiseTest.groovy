package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Stepwise
class InvoiceControllerStepwiseTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    @Shared
    private int invoiceId = 1

    @Shared
    def originalInvoice = TestHelpers.invoice(invoiceId)

    private updatedDate = LocalDate.of(2021, 07, 02)
    private static final ENDPOINT = "/invoices"



    def "empty array is returned when no invoices were created"() {

        when:
        def response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        then:
        response == "[]"
    }

    def "invoice is successfully added to the database"() {

        given:
        def invoiceAsJsonString = jsonService.objectToJsonString(originalInvoice)

        when:
        def invoiceId = Integer.valueOf(mockMvc.perform(post(ENDPOINT)
                .content(invoiceAsJsonString)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString)

        then:
        invoiceId > 0
    }

    def "one invoice is returned when getting all invoices"() {

        given:
        def expectedInvoice = originalInvoice

        when:
        def response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def invoices = jsonService.stringToObject(response, Invoice[])

        then:
        invoices.size() == 1
        invoices[0] == expectedInvoice
    }

    def "invoice is returned correctly when getting by id"() {

        given:
        def expectedInvoice = originalInvoice

        when:
        def response = mockMvc.perform(get("$ENDPOINT/1"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def receivedInvoice = jsonService.stringToObject(response, Invoice)

        then:
        receivedInvoice == expectedInvoice
    }

    def "invoice date is successfully updated"() {

        given:
        def updatedInvoice = originalInvoice
        updatedInvoice.date = updatedDate

        def invoiceAsJsonString = jsonService.objectToJsonString(updatedInvoice)

        expect:
        mockMvc.perform(put("$ENDPOINT/1")
                .content(invoiceAsJsonString)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
    }

    def "updated invoice is returned correctly when getting by id"() {
        given:
        def expectedInvoice = originalInvoice
        expectedInvoice.date = updatedDate

        when:
        def response = mockMvc.perform(get("$ENDPOINT/1"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def receivedInvoice = jsonService.stringToObject(response, Invoice)

        then:
        receivedInvoice == expectedInvoice
    }

    def "invoice is successfully deleted from the database"() {

        expect:
        mockMvc.perform(delete("$ENDPOINT/$invoiceId"))
                .andExpect(status().isNoContent())

        and:
        mockMvc.perform(delete("$ENDPOINT/$invoiceId"))
                .andExpect(status().isNotFound())

        mockMvc.perform(get("$ENDPOINT/$invoiceId"))
                .andExpect(status().isNotFound())
    }
}
