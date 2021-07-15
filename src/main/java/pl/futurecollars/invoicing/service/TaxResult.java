package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class TaxResult {

    BigDecimal income;
    BigDecimal costs;
    BigDecimal incomingVat;
    BigDecimal outgoingVat;
    BigDecimal earnings;
    BigDecimal vatToPay;
}
