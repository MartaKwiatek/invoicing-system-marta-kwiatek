package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @ApiModelProperty(value = "Invoice id (generated by application)", required = true, example = "1")
    private int id;

    @ApiModelProperty(value = "Invoice issue date", required = true, example = "2021-07-28")
    private LocalDate date;

    @ApiModelProperty(value = "Invoice number (assigned by user)", required = true, example = "2021/07/21/00000000001")
    private String number;

    @ApiModelProperty(value = "The company that buys the products", required = true)
    private Company buyer;

    @ApiModelProperty(value = "The company that sells the products", required = true)
    private Company seller;

    @ApiModelProperty(value = "List of products", required = true)
    private List<InvoiceEntry> entries;
}
