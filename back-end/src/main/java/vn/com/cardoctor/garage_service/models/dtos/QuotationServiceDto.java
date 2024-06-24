package vn.com.cardoctor.garage_service.models.dtos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class QuotationServiceDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long quotationId;
    private long serviceId;
    private Integer serviceVersion;
    private String name;
    private Long garageId;
    private Long warrantyPeriod;
    private BigDecimal price;
    private String description;
    private int status;

}
