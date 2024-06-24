package vn.com.cardoctor.garage_service.entities.orders_part;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "sell_spare_part_products")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellSparePartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellSparePartId;
    private Long productId;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private Integer status;
    private Long outboundProductId;
    private Long outboundTicketId;
}
