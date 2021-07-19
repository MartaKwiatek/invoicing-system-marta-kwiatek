package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;

@Service
@AllArgsConstructor
public class TaxCalculatorService {

    private final Database database;

    public BigDecimal income(String taxIdNumber) {
        return database.visit(sellerPredicate(taxIdNumber), InvoiceEntry::getPrice);
    }

    public BigDecimal costs(String taxIdNumber) {
        return database.visit(buyerPredicate(taxIdNumber), this::getCostsIncludingPersonalExpense);
    }

    private BigDecimal getCostsIncludingPersonalExpense(InvoiceEntry invoiceEntry) {
        return invoiceEntry.getPrice()
                .add(invoiceEntry.getVatValue())
                .subtract(getVatValueIncludingPersonalExpense(invoiceEntry));
    }

    public BigDecimal earnings(String taxIdNumber) {
        return income(taxIdNumber).subtract(costs(taxIdNumber));
    }

    public BigDecimal incomingVat(String taxIdNumber) {
        return database.visit(sellerPredicate(taxIdNumber), InvoiceEntry::getVatValue);
    }

    public BigDecimal outgoingVat(Company company) {
        return database.visit(buyerPredicate(company.getTaxIdNumber()), this::getVatValueIncludingPersonalExpense);
    }

    private BigDecimal getVatValueIncludingPersonalExpense(InvoiceEntry invoiceEntry) {
        return invoiceEntry.getCarExpense().isIncludingPrivateExpense()
                ? invoiceEntry.getVatValue().multiply(BigDecimal.valueOf(0.5).setScale(2, RoundingMode.FLOOR)) :
                invoiceEntry.getVatValue();
    }

    public BigDecimal vatToPay(Company company) {
        return incomingVat(company.getTaxIdNumber()).subtract(outgoingVat(company));
    }

    public TaxResult calculateTaxes(Company company) {
        BigDecimal earningsMinusPensionInsurance = earnings(company.getTaxIdNumber()).subtract(company.getPensionInsurance());
        BigDecimal taxCalculationBase = earningsMinusPensionInsurance.setScale(0, RoundingMode.HALF_DOWN);
        BigDecimal incomeTax = taxCalculationBase.multiply(BigDecimal.valueOf(19.00));
        BigDecimal healthInsuranceFullAmount = company.getHealthInsurance().multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(9), RoundingMode.HALF_UP);
        BigDecimal healthInsuranceAmountToReduceTax = healthInsuranceFullAmount.multiply(BigDecimal.valueOf(7.75))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
        BigDecimal incomeTaxMinusHealthInsurance = incomeTax.subtract(healthInsuranceAmountToReduceTax);
        BigDecimal finalIncomeTax = incomeTaxMinusHealthInsurance.setScale(0, RoundingMode.HALF_UP);

        return TaxResult.builder()
                .income(income(company.getTaxIdNumber()))
                .costs(costs(company.getTaxIdNumber()))
                .earnings(earnings(company.getTaxIdNumber()))
                .incomingVat(incomingVat(company.getTaxIdNumber()))
                .outgoingVat(outgoingVat(company))
                .vatToPay(vatToPay(company))
                .pensionInsurance(company.getPensionInsurance())
                .healthInsurance(company.getHealthInsurance())
                .earningsMinusPensionInsurance(earningsMinusPensionInsurance)
                .taxCalculationBase(taxCalculationBase)
                .incomeTax(incomeTax)
                .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthInsurance)
                .finalIncomeTax(finalIncomeTax)
                .build();
    }

    private Predicate<Invoice> buyerPredicate(String taxIdNumber) {
        return invoice -> invoice.getBuyer().getTaxIdNumber().equals(taxIdNumber);
    }

    private Predicate<Invoice> sellerPredicate(String taxIdNumber) {
        return invoice -> invoice.getSeller().getTaxIdNumber().equals(taxIdNumber);
    }
}
