package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "customer_cars")
@IdClass(CustomerCar.class)
@Data
public class CustomerCar implements Serializable {
    @Id
    private long customerId;

    @Id
    private long carId;
}
