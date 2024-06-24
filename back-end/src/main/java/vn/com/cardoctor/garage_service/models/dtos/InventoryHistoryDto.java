package vn.com.cardoctor.garage_service.models.dtos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class InventoryHistoryDto {
    private long id;
    private String counterName;
    private Date countDate;
    private BigDecimal errorRate;
    private Long inventoryId;
}
