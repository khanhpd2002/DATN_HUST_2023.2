package vn.com.cardoctor.garage_service.repositories.orders_part;

import model.PagingDataResponse;

import java.util.Date;

public interface OrderDistributorRepositoryCustom {
    PagingDataResponse findAllOrderDistributor(Long garageId, String orderCode, Long distributorId, String distributorCode, String distributorName, Date fromDate, Date toDate,
                                               Integer deliveryStatus, Integer paymentStatus, Integer pageSize, Integer pageNumber);
}
