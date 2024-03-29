package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InvoiceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @JoinColumn(name = "entry_id")
    @ApiModelProperty(value = "Invoice entry id (generated by application)", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "Product description", required = true, example = "Praying books")
    private String description;

    @ApiModelProperty(value = "Net price", required = true, example = "1000")
    private BigDecimal price;

    @Builder.Default
    @ApiModelProperty(value = "Tax Value", required = true, example = "230")
    private BigDecimal vatValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Tax rate", required = true, example = "VAT_23")
    private Vat vatRate;

    @JoinColumn(name = "car_expense")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @ApiModelProperty(value = "Optional car expense")
    private Car carExpense;
}
