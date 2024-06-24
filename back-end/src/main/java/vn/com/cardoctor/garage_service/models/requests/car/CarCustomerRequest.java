package vn.com.cardoctor.garage_service.models.requests.car;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.Customer;

import javax.persistence.Transient;
import java.util.Objects;

@Data
public class CarCustomerRequest {
    private String phone;
    private String name;
    private Long customerTypeId;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
    private String otherCar;
    private String licensePLate;
    private String vinNumber;
    private Long garageId;

    @Transient
    private String errors;

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
        CarCustomerRequest other = (CarCustomerRequest) obj;
        if (licensePLate == null) {
            return other.licensePLate == null;
        } else return licensePLate.equals(other.licensePLate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePLate);
    }
}
