package pl.futurecollars.invoicing.db.sql;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import pl.futurecollars.invoicing.model.Company;

import java.sql.PreparedStatement;

@AllArgsConstructor
public class AbstractSqlDatabase {

    protected final JdbcTemplate jdbcTemplate;

    protected int insertCompany(Company company) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into company "
                    + "(tax_identification_number, address, name, health_insurance, pension_insurance) "
                    + "values (?, ?, ?, ?, ?);", new String[]{"id"});
            ps.setString(1, company.getTaxIdNumber());
            ps.setString(2, company.getAddress());
            ps.setString(3, company.getName());
            ps.setBigDecimal(4, company.getHealthInsurance());
            ps.setBigDecimal(5, company.getPensionInsurance());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    protected void updateCompany(Company updated) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("update company "
                    + "set tax_identification_number = ?, address = ?, name = ?, health_insurance = ?, pension_insurance = ? "
                    + "where id = ?");

            ps.setString(1, updated.getTaxIdNumber());
            ps.setString(2, updated.getAddress());
            ps.setString(3, updated.getName());
            ps.setBigDecimal(4, updated.getHealthInsurance());
            ps.setBigDecimal(5, updated.getPensionInsurance());
            ps.setLong(6, updated.getId());
            return ps;
        });
    }
}
