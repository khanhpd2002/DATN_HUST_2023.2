package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "service_tags")
@IdClass(ServiceTagId.class)
public class ServiceTag implements Serializable {
    @Id
    private long serviceId;

    @Id
    private long tagId;
}
