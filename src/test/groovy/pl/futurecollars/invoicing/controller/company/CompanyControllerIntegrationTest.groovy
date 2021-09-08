package pl.futurecollars.invoicing.controller.company

import org.springframework.http.MediaType
import pl.futurecollars.invoicing.controller.AbstractControllerTest
import pl.futurecollars.invoicing.model.Company
import spock.lang.Unroll

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.company

@Unroll
class CompanyControllerIntegrationTest extends AbstractControllerTest{

    def setup() {
        deleteAllCompanies()
    }

    def "empty array is returned when no companies were created"() {
        expect:
        getAllCompanies() == []
    }

    def "more than one company is successfully added to the database"() {
        given:
        def howManyCompanies = 10
        List<Company> companiesToAdd = addCompanies(howManyCompanies)

        when:
        def companies = getAllCompanies()

        then:
        howManyCompanies == companies.size()
        companiesToAdd == companies
    }

    def "returns correct ids when companies added"() {
        expect:
        def id = addOneCompany(company(1))
        addOneCompany(company(2)) == id + 1
        addOneCompany(company(3)) == id + 2
        addOneCompany(company(4)) == id + 3
        addOneCompany(company(5)) == id + 4
        addOneCompany(company(6)) == id + 5
    }

    def "getting existing company by id returns appropriate company"() {
        when:
        List<Company> companiesToAdd = addCompanies(10)
        Company companyToFind = companiesToAdd.get(4)

        then:
        getCompanyById(companyToFind.getId()) == companyToFind
    }

    def "returns 404 not found status when trying to get company by non-existent id [#id]"() {
        given:
        addCompanies(10)

        expect:
        mockMvc.perform(get("$COMPANIES_ENDPOINT/$id"))
                .andExpect(status().isNotFound())

        where:
        id << [-100, -2, -1, 0, 1256]
    }

    def "all companies are successfully deleted from the database"() {
        given:
        addCompanies(200)
        getAllCompanies().size() == 200

        when:
        deleteAllCompanies()

        then:
        getAllCompanies().size() == 0
    }

    def "returns 404 not found status when trying to delete company by non-existent id [#id]"() {
        given:
        addCompanies(10)

        expect:
        mockMvc.perform(delete("$COMPANIES_ENDPOINT/$id").with(csrf()))
                .andExpect(status().isNotFound())

        where:
        id << [-100, -2, -1, 0, 1256]
    }

    def "company is successfully updated"() {
        given:
        def id = addOneCompany(company(1))
        def updatedCompany = company(69)
        updatedCompany.id = id
        def updatedCompanyAsJson = jsonService.objectToJsonString(updatedCompany)

        expect:
        mockMvc.perform(put("$COMPANIES_ENDPOINT/$id").with(csrf())
                .content(updatedCompanyAsJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())

        getCompanyById(id) == updatedCompany
    }

    def "returns 404 not found status when trying to update company by non-existent id [#id]"() {
        given:
        addCompanies(10)
        def updatedCompany = jsonService.objectToJsonString(company(9))

        expect:
        mockMvc.perform(put("$COMPANIES_ENDPOINT/$id").with(csrf())
                .content(updatedCompany)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())

        where:
        id << [-76, -1, 0, 5000]
    }
}
