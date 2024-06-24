package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "outbound_products")
public class OutboundProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long outboundTicketId;
    private Long productId;
    private String unit;
    private Long distributorId;
    private BigDecimal requestQuantity;
    private BigDecimal exportQuantity;
    private String note;
    private Integer status;
}
