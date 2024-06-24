package vn.com.cardoctor.garage_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import model.PagingDataResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.InventoryHistory;
import vn.com.cardoctor.garage_service.entities.InventoryHistoryDetail;
import vn.com.cardoctor.garage_service.events.EventPublisher;
import vn.com.cardoctor.garage_service.models.dtos.InventoryHistoryDetailDto;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryHistoryDetailRequest;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryHistoryRequest;
import vn.com.cardoctor.garage_service.models.responses.inventories.InventoryHistoryDetailResponse;
import vn.com.cardoctor.garage_service.repositories.InventoryHistoryDetailRepository;
import vn.com.cardoctor.garage_service.repositories.InventoryHistoryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InventoryHistoryService {
    private final InventoryHistoryRepository inventoryHistoryRepository;

    private final InventoryHistoryDetailRepository inventoryHistoryDetailRepository;

    private final ModelMapper modelMapper;

    private final EventPublisher eventPublisher;

    public PagingDataResponse findAllInventoryHistory(Long inventoryId, Date fromDate, Date toDate, Integer pageSize, Integer pageNumber) {
        return this.inventoryHistoryRepository.findAllInventoryHistory(inventoryId, fromDate, toDate, pageSize, pageNumber);
    }

    public InventoryHistoryDetailResponse findHistoryDetail(Long inventoryHistoryId, boolean isDifferentQuantity) throws ApiException, JsonProcessingException {
        InventoryHistoryDetailResponse response = new InventoryHistoryDetailResponse();
        Optional<InventoryHistory> oInventoryHistory = this.inventoryHistoryRepository.findById(inventoryHistoryId);
        if (oInventoryHistory.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin lần kiểm kê này");
        }
        InventoryHistory inventoryHistory = oInventoryHistory.get();
        response.setCountDate(inventoryHistory.getCountDate());
        ObjectMapper mapper = new ObjectMapper();
        List<InventoryHistoryRequest.Counter> countersName = Arrays.asList(mapper.readValue(inventoryHistory.getCountersName(), InventoryHistoryRequest.Counter[].class));
        List<InventoryHistoryRequest.Approver> approvers = inventoryHistory.getApprovers() != null ? Arrays.asList(mapper.readValue(inventoryHistory.getApprovers(), InventoryHistoryRequest.Approver[].class)) : new ArrayList<>();
        response.setCountersName(countersName);
        response.setApprovers(approvers);
        response.setCanAdjustment(inventoryHistory.getCanAdjustment());
        List<InventoryHistoryDetailDto> inventoryHistoryDetails = this.inventoryHistoryDetailRepository.findHistoryDetail(inventoryHistoryId, isDifferentQuantity);
        response.setInventoryHistoryDetails(inventoryHistoryDetails);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(InventoryHistoryRequest inventoryHistoryRequest, Long inventoryId) {
        InventoryHistory inventoryHistory = new InventoryHistory();
        String countersName = new Gson().toJson(inventoryHistoryRequest.getCountersName());
        String approvers = new Gson().toJson(new ArrayList<>());
        inventoryHistory.setCountersName(countersName);
        inventoryHistory.setApprovers(approvers);
        inventoryHistory.setCountDate(inventoryHistoryRequest.getCountDate());
        inventoryHistory.setInventoryId(inventoryId);
        inventoryHistory = this.inventoryHistoryRepository.save(inventoryHistory);
        BigDecimal totalSystemInventory = new BigDecimal(0);
        BigDecimal totalRealityInventory = new BigDecimal(0);

        List<InventoryHistoryDetail> inventoryHistoryDetails = new ArrayList<>();
        for (InventoryHistoryDetailRequest inventoryHistoryDetailRequest : inventoryHistoryRequest.getInventoryHistoryDetails()) {
            InventoryHistoryDetail inventoryHistoryDetail = new InventoryHistoryDetail();
            inventoryHistoryDetail.setInventoryHistoryId(inventoryHistory.getId());
            inventoryHistoryDetail.setProductId(inventoryHistoryDetailRequest.getProductId());
            inventoryHistoryDetail.setSystemUnitPrice(inventoryHistoryDetailRequest.getSystemUnitPrice());
            inventoryHistoryDetail.setRealityUnitPrice(inventoryHistoryDetailRequest.getRealityUnitPrice());
            inventoryHistoryDetail.setSystemInventory(inventoryHistoryDetailRequest.getSystemInventory());
            totalSystemInventory = totalSystemInventory.add(inventoryHistoryDetailRequest.getSystemInventory());
            if (inventoryHistoryDetailRequest.getRealityInventory() == null) {
                inventoryHistoryDetailRequest.setRealityInventory(BigDecimal.ZERO);
            }
            inventoryHistoryDetail.setRealityInventory(inventoryHistoryDetailRequest.getRealityInventory());
            totalRealityInventory = totalRealityInventory.add(inventoryHistoryDetailRequest.getRealityInventory());
            inventoryHistoryDetails.add(inventoryHistoryDetail);
        }
        if (totalSystemInventory.equals(BigDecimal.ZERO)) {
            inventoryHistory.setErrorRate(BigDecimal.ONE);
        } else {
            BigDecimal errorRate = totalRealityInventory.divide(totalSystemInventory, 2, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
            inventoryHistory.setErrorRate(errorRate);
        }
        this.inventoryHistoryRepository.save(inventoryHistory);
        this.inventoryHistoryDetailRepository.saveAll(inventoryHistoryDetails);
        this.eventPublisher.publishCreateNewInventoryHistoryEvent("create_new_inventory_history", inventoryHistory.getId(), inventoryId);
        return inventoryHistory.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(InventoryHistoryRequest inventoryHistoryRequest, Long inventoryId, Long inventoryHistoryId) throws JsonProcessingException, ApiException {
        Optional<InventoryHistory> oInventoryHistory = this.inventoryHistoryRepository.findById(inventoryHistoryId);
        if (oInventoryHistory.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        if (inventoryHistoryRequest.getApprovers().isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin người kiểm duyệt");
        }
        InventoryHistory inventoryHistory = oInventoryHistory.get();
        String countersName = new Gson().toJson(inventoryHistoryRequest.getCountersName());
        String approvers = new Gson().toJson(inventoryHistoryRequest.getApprovers());
        inventoryHistory.setCountersName(countersName);
        inventoryHistory.setApprovers(approvers);
        inventoryHistory.setCountDate(inventoryHistoryRequest.getCountDate());
        inventoryHistory.setInventoryId(inventoryId);
        BigDecimal totalSystemInventory = new BigDecimal(0);
        BigDecimal totalRealityInventory = new BigDecimal(0);

        this.inventoryHistoryDetailRepository.deleteAllByInventoryHistoryId(inventoryHistoryId);
        List<InventoryHistoryDetail> inventoryHistoryDetails = new ArrayList<>();
        for (InventoryHistoryDetailRequest inventoryHistoryDetailRequest : inventoryHistoryRequest.getInventoryHistoryDetails()) {
            InventoryHistoryDetail inventoryHistoryDetail = new InventoryHistoryDetail();
            inventoryHistoryDetail.setInventoryHistoryId(inventoryHistory.getId());
            inventoryHistoryDetail.setProductId(inventoryHistoryDetailRequest.getProductId());
            inventoryHistoryDetail.setSystemUnitPrice(inventoryHistoryDetailRequest.getSystemUnitPrice());
            inventoryHistoryDetail.setRealityUnitPrice(inventoryHistoryDetailRequest.getRealityUnitPrice());
            inventoryHistoryDetail.setSystemInventory(inventoryHistoryDetailRequest.getSystemInventory());
            totalSystemInventory = totalSystemInventory.add(inventoryHistoryDetailRequest.getSystemInventory());
            inventoryHistoryDetail.setRealityInventory(inventoryHistoryDetailRequest.getRealityInventory());
            totalRealityInventory = totalRealityInventory.add(inventoryHistoryDetailRequest.getRealityInventory());
            inventoryHistoryDetails.add(inventoryHistoryDetail);
        }
        BigDecimal errorRate = totalRealityInventory.divide(totalSystemInventory, 2, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
        inventoryHistory.setErrorRate(errorRate);
        this.inventoryHistoryRepository.save(inventoryHistory);
        this.inventoryHistoryDetailRepository.saveAll(inventoryHistoryDetails);
        return inventoryHistory.getId();
    }

    public Long adjustmentQuantity(InventoryHistoryRequest inventoryHistoryRequest, Long inventoryId, Long inventoryHistoryId) throws ApiException {
        Optional<InventoryHistory> oInventoryHistory = this.inventoryHistoryRepository.findById(inventoryHistoryId);
        if (oInventoryHistory.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
//        if (inventoryHistoryRequest.getApprovers().isEmpty()) {
//            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin người kiểm duyệt");
//        }
        InventoryHistory inventoryHistory = oInventoryHistory.get();
        inventoryHistory.setAdjustmentDate(inventoryHistoryRequest.getAdjustmentDate());
        inventoryHistory.setInventoryId(inventoryId);
        inventoryHistory.setCanAdjustment(Boolean.FALSE);
        this.inventoryHistoryRepository.save(inventoryHistory);
        this.eventPublisher.publishAdjustmentInventoryEvent("adjustment_inventory_event", inventoryHistoryRequest);
        return inventoryHistoryId;
    }

    public PagingDataResponse getDetailHistory(Long inventoryId, Long historyId, Integer pageSize, Integer pageNumber) {
        return this.inventoryHistoryRepository.getDetailHistory(historyId, pageSize, pageNumber);
    }

}
