package vn.com.cardoctor.garage_service.controllers.garages;

import authentication.AcAuthentication;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.garage_service.EServiceRequest;
import vn.com.cardoctor.garage_service.models.responses.e_service.EServiceResponse;
import vn.com.cardoctor.garage_service.services.garages.EServiceService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/e-services")
public class EServiceController {
    @Autowired
    EServiceService eServiceService;

    @GetMapping
    public PagingDataResponse getAllEService(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = false) Integer status,
                                                  @RequestParam Integer pageSize,
                                                  @RequestParam Integer pageNumber) throws ApiException {
        return this.eServiceService.getAllEService(name, description, status, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse<EServiceResponse> getEService(@PathVariable("id") Long serviceId) throws ApiException {
        BaseResponse<EServiceResponse> response = new BaseResponse<>();
        response.setData(this.eServiceService.getEService(serviceId));
        return response;
    }

    @PostMapping
    public BaseResponse<Long> createEService(@RequestBody EServiceRequest eServiceRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(eServiceService.createEService(eServiceRequest));
        return response;
    }

    @AcAuthentication(enableAuthentication = true, applyClient = {"cardoctor-admin"})
    @PatchMapping("/{id}")
    public BaseResponse<Long> updateEService(@PathVariable("id") Long serviceId,
                                                  @RequestBody EServiceRequest eServiceRequest,
                                                  HttpServletRequest httpServletRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(eServiceService.updateEService(serviceId, eServiceRequest, httpServletRequest));
        return response;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<String> removeEService(@PathVariable("id") Long serviceId) throws ApiException {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(eServiceService.removeEService(serviceId));
        return response;
    }

    @PostMapping("/accept")
    public BaseResponse<String> acceptList(@RequestBody List<Long> ids) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(eServiceService.acceptList(ids));
        return response;
    }

    @PostMapping("/reject")
    public BaseResponse<String> rejectList(@RequestBody List<Long> ids) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(eServiceService.rejectList(ids));
        return response;
    }
}
