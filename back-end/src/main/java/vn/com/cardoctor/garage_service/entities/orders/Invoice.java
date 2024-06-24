package vn.com.cardoctor.garage_service.entities.orders;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Data
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long repairOrderId;
    private Integer paymentType;
    private int paymentStatus;
    private String images;
    private Long createdBy;
    private Long updatedBy;
}
