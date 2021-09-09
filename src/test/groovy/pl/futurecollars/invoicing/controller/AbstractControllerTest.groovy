package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.TaxResult
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
class AbstractControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    JsonService jsonService

    static final String INVOICES_ENDPOINT = "/invoices"
    static final String COMPANIES_ENDPOINT = "/companies"
    static final String TAXES_ENDPOINT = "/taxes"

    def setup() {
        deleteAllInvoices()
        deleteAllCompanies()
    }

    int addOneInvoice(Invoice invoice) {
        addOneItem(invoice, INVOICES_ENDPOINT)
    }

    int addOneCompany(Company company) {
        addOneItem(company, COMPANIES_ENDPOINT)
    }

    private <T> int addOneItem(T item, String endpoint) {
        def itemAsJsonString = jsonService.objectToJsonString(item)

        String id = mockMvc.perform(post(endpoint)
                .content(itemAsJsonString)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return id as int
    }

    List<Invoice> addInvoices(int howMany) {
        (1..howMany).collect({ id ->
            def invoice = TestHelpers.invoice(id)
            invoice.id = addOneInvoice(invoice)
            return invoice
        })
    }

    List<Company> addCompanies(int howMany) {
        (1..howMany).collect({ id ->
            def company = TestHelpers.company(id)
            company.id = addOneCompany(company)
            return company
        })
    }

    List<Invoice> getAllInvoices() {
        getAllItems(Invoice[], INVOICES_ENDPOINT)
    }

    List<Company> getAllCompanies() {
        getAllItems(Company[], COMPANIES_ENDPOINT)
    }

    private <T> T getAllItems(Class<T> clazz, String endpoint) {
        def response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return jsonService.stringToObject(response, clazz)
    }

    Invoice getInvoiceById(long id) {
        getItemById(id, Invoice, INVOICES_ENDPOINT)
    }

    Company getCompanyById(long id) {
        getItemById(id, Company, COMPANIES_ENDPOINT)
    }

    private <T> T getItemById(long id, Class<T> clazz, String endpoint) {
        def response = mockMvc.perform(get("$endpoint/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        return jsonService.stringToObject(response, clazz)
    }

    void deleteInvoice(long id) {
        mockMvc.perform(delete("$INVOICES_ENDPOINT/$id").with(csrf()))
                .andExpect(status().isNoContent())
    }

    void deleteCompany(long id) {
        mockMvc.perform(delete("$COMPANIES_ENDPOINT/$id").with(csrf()))
                .andExpect(status().isNoContent())
    }

    void deleteAllInvoices() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    void deleteAllCompanies() {
        getAllCompanies().each { company -> deleteCompany(company.id) }
    }

    TaxResult calculateTax(Company company) {
        def response = mockMvc.perform(post("$TAXES_ENDPOINT")
                .content(jsonService.objectToJsonString(company))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return jsonService.stringToObject(response, TaxResult)
    }
}
