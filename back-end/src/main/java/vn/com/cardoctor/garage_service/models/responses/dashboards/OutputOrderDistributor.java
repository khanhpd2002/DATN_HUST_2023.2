package vn.com.cardoctor.garage_service.models.responses.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputOrderDistributor {
    private Long distributorId;
    private String distributorCode;
    private String distributorName;
    private BigDecimal output;
}