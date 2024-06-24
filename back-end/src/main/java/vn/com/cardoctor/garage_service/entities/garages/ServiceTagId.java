package vn.com.cardoctor.garage_service.entities.garages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceTagId implements Serializable {
    private long serviceId;
    private long tagId;
}
