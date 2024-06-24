package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRepairOrderDto {
    private Long id;
    private String code;
    private Long garageId;
    private String garageName;
    private String garageOwnerPhone;
    private Date appointmentDate;
    private BigDecimal totalPrice;
    private Integer status;
}
