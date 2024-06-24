package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairOrderDto {
    private long id;
    private String code;
    private Long customerId;
    private String customerName;
    private Long carId;
    private String licensePlate;
    private long garageId;
    private String garageName;
    private String customerTypeName;
    private Date appointmentDate;
    private Integer status;
    private Integer paymentStatus;
    private BigDecimal totalPrice;

}
