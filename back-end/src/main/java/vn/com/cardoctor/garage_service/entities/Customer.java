package vn.com.cardoctor.garage_service.entities;


import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.cardoctor.garage_service.models.dtos.CustomerDto;

import javax.persistence.*;

@SqlResultSetMapping(
        name = "CustomerDto",
        classes = {
                @ConstructorResult(
                        targetClass = CustomerDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "customer_type_id", type = Long.class),
                                @ColumnResult(name = "customer_type_name", type = String.class),
                                @ColumnResult(name = "full_name", type = String.class),
                                @ColumnResult(name = "phone_number", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                                @ColumnResult(name = "garage_id", type = Long.class),
                                @ColumnResult(name = "driver_id", type = Long.class),
                        })})

@Entity
@Data
@Table(name = "customers")
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long customerTypeId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Long garageId;
    private Long driverId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Customer other = (Customer) obj;
        // Check if the phone numbers are equal
        if (phoneNumber == null) {
            return other.phoneNumber == null;
        } else return phoneNumber.equals(other.phoneNumber);
    }
}
