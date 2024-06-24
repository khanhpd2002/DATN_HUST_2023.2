package vn.com.cardoctor.garage_service.repositories;

import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.models.dtos.ProductRefundedDto;
import vn.com.cardoctor.garage_service.models.responses.dashboards.CountProductResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface DashboardRepository {
    CountProductResponse getTotalProduct(Long garageId, Date fromDate, Date toDate);

    BigDecimal getInventoryValue(Long garageId, Date fromDate, Date toDate);

    Long getTotalTicketHasNotDelivery(Long garageId, Date fromDate, Date toDate);

    List<ProductRefundedDto> getMostProductRefunded(Long garageId);
}
