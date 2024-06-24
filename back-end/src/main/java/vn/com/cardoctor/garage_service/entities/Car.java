package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.CarDto;

import javax.persistence.*;

@SqlResultSetMapping(
        name = "CarDto",
        classes = {
                @ConstructorResult(
                        targetClass = CarDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "car_name", type = String.class),
                                @ColumnResult(name = "car_brand_id", type = Long.class),
                                @ColumnResult(name = "car_brand_name", type = String.class),
                                @ColumnResult(name = "car_model_id", type = Long.class),
                                @ColumnResult(name = "car_model_name", type = String.class),
                                @ColumnResult(name = "car_year_id", type = Long.class),
                                @ColumnResult(name = "car_year_name", type = String.class),
                                @ColumnResult(name = "car_version_id", type = Long.class),
                                @ColumnResult(name = "car_version_name", type = String.class),
                                @ColumnResult(name = "customer_id", type = Long.class),
                                @ColumnResult(name = "license_plate", type = String.class),
                                @ColumnResult(name = "vin_number", type = String.class),
                                @ColumnResult(name = "customer_name", type = String.class),
                                @ColumnResult(name = "customer_phone", type = String.class),
                                @ColumnResult(name = "customer_type_name", type = String.class),
                                @ColumnResult(name = "garage_id", type = Long.class)
                        })})

@Entity
@Data
@Table(name = "cars")
public class Car extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String carName;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
    private String licensePlate;
    private String vinNumber;
    private Long customerId;
    private Long garageId;
    private Long driverCarId;

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
        Car other = (Car) obj;
        // Check if the phone numbers are equal
        if (licensePlate == null || vinNumber == null) {
            return false;
        } else return licensePlate.equals(other.licensePlate) || vinNumber.equals(other.vinNumber);
    }
}
