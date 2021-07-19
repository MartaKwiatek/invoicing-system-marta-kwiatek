package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceEntry {

    @ApiModelProperty(value = "Product description", required = true, example = "Praying books")
    private String description;

    @ApiModelProperty(value = "Net price", required = true, example = "1000")
    private BigDecimal price;

    @Builder.Default
    @ApiModelProperty(value = "Tax Value", required = true, example = "230")
    private BigDecimal vatValue = BigDecimal.ZERO;

    @ApiModelProperty(value = "Tax rate", required = true, example = "VAT_23")
    private Vat vatRate;

    @ApiModelProperty(value = "Optional car expense")
    private Car carExpense;
}