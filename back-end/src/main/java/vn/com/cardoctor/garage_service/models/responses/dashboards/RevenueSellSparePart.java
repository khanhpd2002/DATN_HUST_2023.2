package vn.com.cardoctor.garage_service.models.responses.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueSellSparePart {

    private Date date;
    private String stringDate;
    private BigDecimal revenueSellSparePart;
}
