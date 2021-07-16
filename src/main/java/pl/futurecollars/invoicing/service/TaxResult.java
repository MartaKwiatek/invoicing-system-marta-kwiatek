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
    private BigDecimal incomingVat;
    private BigDecimal outgoingVat;
    private BigDecimal earnings;
    private BigDecimal vatToPay;
}
