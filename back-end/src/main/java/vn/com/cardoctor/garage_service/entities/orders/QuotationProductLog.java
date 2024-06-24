package vn.com.cardoctor.garage_service.entities.orders;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "quotation_product_logs")
@Data
public class QuotationProductLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quotationId;
    private Integer logVersion;
    private String content;
}
