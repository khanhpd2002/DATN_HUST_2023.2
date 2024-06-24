package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "custom_field_values")
@Data
public class CustomFieldValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long customFieldId;
    private String textValue;
    private Long optionValue;
}
