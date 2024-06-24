package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "custom_field_options")
@Data
public class CustomFieldOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long customFieldId;
    private String name;
}
