package vn.com.cardoctor.garage_service.entities.rest;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Area {
    @Id
    private Long id;

    private Long provinceId;

    private Long districtId;

    private Long wardId;
}
