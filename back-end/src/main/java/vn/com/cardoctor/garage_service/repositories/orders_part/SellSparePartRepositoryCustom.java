package vn.com.cardoctor.garage_service.repositories.orders_part;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.models.requests.orders_part.SellSparePartFilter;
import vn.com.cardoctor.garage_service.models.responses.dashboards.RevenueSellSparePart;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface SellSparePartRepositoryCustom {
    PagingDataResponse findAllSellSparePart(Long garageId, SellSparePartFilter sparePartFilter, Integer pageSize, Integer pageNumber);

    BigDecimal getTotalPriceByGarageId(Long garageId, String fromDate, String toDate);

    List<RevenueSellSparePart> findRevenueOfSellSparePart(String groupByType, String fromDate, String toDate, Long garageId);

    List<RevenueSellSparePart> findRevenueOfSellSparePartV2(String groupByType, Long garageId);
}
