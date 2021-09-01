package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.futurecollars.invoicing.db.WithId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Invoice implements WithId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Invoice id (generated by application)", required = true)
    private Long id;

    @ApiModelProperty(value = "Invoice issue date", required = true, example = "2021-07-28")
    private LocalDate date;

    @ApiModelProperty(value = "Invoice number (assigned by user)", required = true, example = "2021/07/21/00000000001")
    private String number;

    @JoinColumn(name = "buyer")
    @OneToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(value = "The company that buys the products", required = true)
    private Company buyer;

    @JoinColumn(name = "seller")
    @OneToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(value = "The company that sells the products", required = true)
    private Company seller;

    @JoinTable(name = "invoice_invoice_entry", inverseJoinColumns = @JoinColumn(name = "invoice_entry_id"))
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ApiModelProperty(value = "List of products", required = true)
    private List<InvoiceEntry> entries;
}
