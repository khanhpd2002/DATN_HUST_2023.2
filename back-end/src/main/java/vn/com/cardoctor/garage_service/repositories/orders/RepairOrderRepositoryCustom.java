package vn.com.cardoctor.garage_service.repositories.orders;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.models.dtos.RevenueRepairDto;
import vn.com.cardoctor.garage_service.models.requests.repair_order.FilterRepairOrder;
import vn.com.cardoctor.garage_service.models.responses.DashboardResponse;
import vn.com.cardoctor.garage_service.models.responses.TypeServicePopularResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RepairOrderRepositoryCustom {
    PagingDataResponse getAllRepairOrder(FilterRepairOrder filterRepairOrder);

    List<Long> getExpertIdsInTicket();

    Long getExpertIdMostFreeInTicket(List<Long> expertIdsCms);

    List<Long> getOperatorIdsInTicket();

    Long getOperatorIdMostFreeInTicket(List<Long> operatorIdsCms);

    BigDecimal getRevenue(Long garageId,List<Integer> statusCancelRepairOrder, String fromDate, String toDate);

    BigDecimal getNumberServiceByStatus(Long garageId, Integer status, String fromDate, String toDate);

    List<DashboardResponse.PopularService> getListPopularServices(Long garageId);

    List<DashboardResponse.ExcellentStaff> getListExcellentStaffs(Long garageId, String fromDate, String toDate);

    List<TypeServicePopularResponse.ListTypeServicePopularResponse> getListTypeServicePopular(Long garageId, String fromDate, String toDate);

    BigDecimal getNumberEmployeeOnRevenue(Long garageId, List<Integer> statusCancelRepairOrder, String fromDate, String toDate);

    List<RevenueRepairDto> findRevenueOfRepair(String groupByType, String fromDate, String toDate, Long garageId);

    List<RevenueRepairDto> findRevenueOfRepairV2(String groupByType, Long garageId);

    PagingDataResponse adminGetAllRepairOrder(String code, String garageOwnerPhone, String garageName, Date fromDate, Date toDate,
                                              Integer pageNumber, Integer pageSize);
}
