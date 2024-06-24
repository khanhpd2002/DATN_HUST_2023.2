package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_compatibilities")
public class ProductCompatibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long productId;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
}
