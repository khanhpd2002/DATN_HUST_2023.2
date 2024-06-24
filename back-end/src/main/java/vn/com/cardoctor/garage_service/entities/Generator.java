package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "generators")
public class Generator extends BaseEntity {
    @Id
    private String code;
    private String value;
}
