package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "custom_fields")
@Data
public class CustomField extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String resourceName;
    private Long modelId;
    private String fieldType;
    private String fieldName;
    private String label;
    private Integer visible;
    private Integer isRequired;
}
