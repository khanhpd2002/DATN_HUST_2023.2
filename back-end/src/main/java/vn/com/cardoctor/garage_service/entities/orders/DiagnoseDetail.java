package vn.com.cardoctor.garage_service.entities.orders;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "diagnoses_detail")
public class DiagnoseDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long diagnoseId;
    private Long productId;
    private BigDecimal quantity;
}
