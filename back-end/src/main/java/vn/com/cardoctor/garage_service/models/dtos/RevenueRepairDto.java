package vn.com.cardoctor.garage_service.models.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RevenueRepairDto {
    private Date date;
    private String stringDate;
    private BigDecimal revenueOfRepair;
}
