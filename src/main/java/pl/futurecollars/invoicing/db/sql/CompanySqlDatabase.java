package pl.futurecollars.invoicing.db.sql;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

public class CompanySqlDatabase extends AbstractSqlDatabase implements Database<Company> {

    public CompanySqlDatabase(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public long save(Company company) {
        return insertCompany(company);
    }

    @Override
    public Optional<Company> getById(long id) {
        List<Company> companies = jdbcTemplate.query("select * from company where id = " + id, companyRowMapper());
        return companies.isEmpty() ? Optional.empty() : Optional.of(companies.get(0));
    }

    @Override
    public List<Company> getAll() {
        return jdbcTemplate.query("select * from company", companyRowMapper());
    }

    @Override
    @Transactional
    public Optional<Company> update(long id, Company updatedCompany) {
        Optional<Company> oldCompany = getById(id);

        if (oldCompany.isEmpty()) {
            return Optional.empty();
        }

        updateCompany(updatedCompany);

        return oldCompany;
    }

    @Override
    @Transactional
    public Optional<Company> delete(long id) {
        Optional<Company> oldCompany = getById(id);

        if (oldCompany.isEmpty()) {
            return Optional.empty();
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "delete from company where id = ?;");
            ps.setLong(1, id);
            return ps;
        });

        return oldCompany;
    }

    private RowMapper<Company> companyRowMapper() {
        return (rs, rowNr) -> Company.builder()
                .id(rs.getLong("id"))
                .taxIdNumber(rs.getString("tax_id"))
                .name(rs.getString("name"))
                .address(rs.getString("address"))
                .healthInsurance(rs.getBigDecimal("health_insurance"))
                .pensionInsurance(rs.getBigDecimal("pension_insurance"))
                .build();
    }
}
