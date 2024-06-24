package vn.com.cardoctor.garage_service.controllers.orders;

import vn.com.cardoctor.garage_service.models.requests.diagnose.DiagnoseRequest;
import vn.com.cardoctor.garage_service.models.responses.diagnose.DiagnoseResponse;
import vn.com.cardoctor.garage_service.services.orders.DiagnoseService;
import model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/repair-orders/{repair-order-id}/diagnoses")
public class DiagnoseController {
    @Autowired
    DiagnoseService diagnoseService;

    @PostMapping
    public BaseResponse<Long> createDiagnoseGms(@PathVariable(name = "repair-order-id") Long repairOrderId,
                                                @RequestBody DiagnoseRequest diagnoseRequest,
                                                @RequestParam(required = false) Boolean isCreateDiagnose) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        if (Boolean.TRUE.equals(isCreateDiagnose)) {
            response.setData(diagnoseService.createDiagnoseGms(repairOrderId, diagnoseRequest, true));
        } else {
            response.setData(diagnoseService.createDiagnoseGms(repairOrderId, diagnoseRequest, false));
        }
        return response;
    }

    @PatchMapping
    public BaseResponse<Long> updateDiagnoseGms(@PathVariable(name = "repair-order-id") Long repairOrderId,
                                                @RequestBody DiagnoseRequest diagnoseRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(diagnoseService.updateDiagnoseGms(repairOrderId, diagnoseRequest));
        return response;
    }
}
