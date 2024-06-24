package vn.com.cardoctor.garage_service.controllers;

import lombok.extern.log4j.Log4j2;
import model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import vn.com.cardoctor.garage_service.entities.dashboards.DataChart;
import vn.com.cardoctor.garage_service.entities.dashboards.LongTermInventoryProduct;
import vn.com.cardoctor.garage_service.models.responses.DashboardResponse;
import vn.com.cardoctor.garage_service.models.responses.TypeServicePopularResponse;
import vn.com.cardoctor.garage_service.models.responses.dashboards.DashboardInventoryResponse;
import vn.com.cardoctor.garage_service.services.DashboardService;

import java.util.Date;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/dashboard")
@Log4j2
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/{garage-id}")
    public BaseResponse<DashboardResponse> index(
            @PathVariable("garage-id") Long garageId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        BaseResponse<DashboardResponse> response = new BaseResponse<>();
        response.setData(dashboardService.getData(garageId, fromDate, toDate));
        return response;
    }

    @GetMapping("/type-service-popular/{garage-id}")
    public BaseResponse<TypeServicePopularResponse> typeServicePopular(
            @PathVariable("garage-id") Long garageId,
            @RequestParam(required = false) String time
    ) {
        BaseResponse<TypeServicePopularResponse> response = new BaseResponse<>();
        response.setData(dashboardService.getListTypeServicePopular(garageId, time));
        return response;
    }

    @GetMapping("/{garage-id}/renevue-repair")
    public BaseResponse<DataChart> getRenevue(@RequestParam String groupByType,
                                              @RequestParam String fromDate,
                                              @RequestParam String toDate,
                                              @PathVariable(name = "garage-id") Long garageId) {
        BaseResponse<DataChart> response = new BaseResponse<>();
        response.setData(this.dashboardService.getRevenue(groupByType, fromDate, toDate, garageId));
        return response;
    }

    @GetMapping("/{garage-id}/summary-inventory")
    public BaseResponse<DashboardInventoryResponse> getSummaryDashboardInventory(@PathVariable(name = "garage-id") Long garageId,
                                                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
        BaseResponse<DashboardInventoryResponse> response = new BaseResponse<>();
        response.setData(this.dashboardService.getSummaryDashboardInventory(garageId, fromDate, toDate));
        return response;
    }

//    String groupByType, String fromDate, String toDate, Long garageId

    @GetMapping("/{garage-id}/output-order-distributor")
    public BaseResponse<DataChart> getOutputOrderDistributors(@PathVariable(name = "garage-id") Long garageId,
                                                              @RequestParam(required = false) Long quantityLabel) {
        BaseResponse<DataChart> response = new BaseResponse<>();
        response.setData(this.dashboardService.getOutputOrderDistributors(garageId, quantityLabel));
        return response;
    }

    @GetMapping("/{garage-id}/most-product-refunded")
    public BaseResponse<DataChart> getMostProductRefunded(@PathVariable(name = "garage-id") Long garageId) {
        BaseResponse<DataChart> response = new BaseResponse<>();
        response.setData(this.dashboardService.getMostProductRefunded(garageId));
        return response;
    }

    @GetMapping("/{garage-id}/low-inventory-products")
    public BaseResponse<DataChart> getLowInventoryProducts(@PathVariable(name = "garage-id") Long garageId,
                                                              @RequestParam(required = false) Long quantityLabel) {
        BaseResponse<DataChart> response = new BaseResponse<>();
        response.setData(this.dashboardService.getLowInventoryProducts(garageId, quantityLabel));
        return response;
    }

    @GetMapping("/{garage-id}/long-term-inventory-products")
    public BaseResponse<Page<LongTermInventoryProduct>> getLongTermInventoryProducts(@PathVariable(name = "garage-id")
                                                                                          Long garageId, Pageable pageable) {
        BaseResponse<Page<LongTermInventoryProduct>> response = new BaseResponse<>();
        response.setData(this.dashboardService.getLongTermInventoryProducts(garageId, pageable));
        return response;

    }
}
