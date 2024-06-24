package vn.com.cardoctor.garage_service.entities.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LongTermInventoryProduct {
    private Long productId;
    private String productName;
    private String productCode;
    private LocalDateTime inboundDate;
}
