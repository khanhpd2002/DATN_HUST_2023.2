package vn.com.cardoctor.garage_service.entities;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "quotation_services")
public class QuotationService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long quotationId;
    private long serviceId;
    private long serviceVersion;
    private Date createdAt;
    private Date updatedAt;

}
