package vn.com.cardoctor.garage_service.entities.orders;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quotations")
@Data
public class Quotation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long repairOrderId;
    private Long diagnoseId;
    private Integer type;
    private Integer status;
    private BigDecimal totalPrice;
    private String images;
    private Integer discountType;
    private Long createdBy;
    private Long updatedBy;
}
