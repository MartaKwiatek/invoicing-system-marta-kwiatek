package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaxResult {

    private BigDecimal income;
    private BigDecimal costs;
    private BigDecimal earnings;

    private BigDecimal incomingVat;
    private BigDecimal outgoingVat;
    private BigDecimal vatToPay;

    private BigDecimal pensionInsurance;
    private BigDecimal healthInsurance;

    private BigDecimal earningsMinusPensionInsurance;
    private BigDecimal taxCalculationBase;
    private BigDecimal incomeTax;
    private BigDecimal incomeTaxMinusHealthInsurance;

    private BigDecimal finalIncomeTax;
}
