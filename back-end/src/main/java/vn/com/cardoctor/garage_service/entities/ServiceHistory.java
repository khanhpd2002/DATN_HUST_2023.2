package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Map;

@Entity
@Data
@Table(name = "service_histories")
public class ServiceHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long serviceId;
    @Column(name = "content")
    @Type(type = "json")
    private Map<String, Object> content;
    private Integer version;
    private String updatedBy;
}
