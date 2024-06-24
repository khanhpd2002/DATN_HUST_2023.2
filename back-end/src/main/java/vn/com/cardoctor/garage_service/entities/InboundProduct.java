package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "inbound_products")
public class InboundProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long inboundTicketId;
    private Long productId;
    private String unit;
    private Long distributorId;
    private BigDecimal requestQuantity;
    private BigDecimal exportQuantity;
    private String note;
    private Integer status;
}
