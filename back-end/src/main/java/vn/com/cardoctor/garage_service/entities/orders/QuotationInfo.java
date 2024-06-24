package vn.com.cardoctor.garage_service.entities.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quotation_infos")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quotationId;
    private Integer type;
    private Long productId;
    private Long garageServiceId;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal originPrice;
    private BigDecimal price;
    private Long employeeId;
    private Integer status;
    private Long outboundProductId;
}
