package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String detail;
    private String reason;
    private String image;
    private Integer status;
    private String itemId;
    private Integer type;
    private Integer priorityLevel;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
