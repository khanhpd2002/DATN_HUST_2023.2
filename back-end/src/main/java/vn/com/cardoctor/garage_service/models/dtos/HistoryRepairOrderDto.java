package vn.com.cardoctor.garage_service.models.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class HistoryRepairOrderDto {
    private long customerId;

    private String customerName;

    private long repairOrderId;

    private String repairOrderDescription;

    private Date appointmentDate;

    private BigDecimal totalPrice;

    private String products;

    private String services;

}
