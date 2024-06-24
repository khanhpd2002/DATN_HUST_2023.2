package vn.com.cardoctor.garage_service.controllers.garages;

import lombok.RequiredArgsConstructor;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.controllers.BaseController;
import vn.com.cardoctor.garage_service.models.requests.employee.EmployeeRequest;
import vn.com.cardoctor.garage_service.models.responses.employee.EmployeeResponse;
import vn.com.cardoctor.garage_service.services.garages.EmployeeService;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("garages/employees")
@RequiredArgsConstructor
public class EmployeeController extends BaseController {
    private final EmployeeService employeeService;

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> createEmployee(@PathVariable("garage-id") Long garageId,
                                             @RequestBody EmployeeRequest request) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(employeeService.createEmployee(garageId, request));
        return response;
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse getEmployee(@PathVariable("garage-id") Long garageId,
                                          @RequestParam(required = false) String fullName,
                                          @RequestParam(required = false) Integer type,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                          @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return employeeService.searchEmployee(garageId, fullName, type, status, startDate, endDate, pageNumber, pageSize);
    }

    @GetMapping
    public PagingDataResponse getEmployeeAdmin(@RequestParam(required = false) Long garageId,
                                               @RequestParam(required = false) String fullName,
                                               @RequestParam(required = false) Integer type,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return employeeService.searchEmployee(garageId, fullName, type, status, null, null, pageNumber, pageSize);
    }

    @GetMapping("/{garage-id}/info/{id}")
    public BaseResponse<EmployeeResponse> getDetailEmployee(@PathVariable("garage-id") Long garageId,
                                                            @PathVariable("id") Long id) throws ApiException {
        BaseResponse<EmployeeResponse> response = new BaseResponse<>();
        response.setData(employeeService.getDetailEmployee(id));
        return response;
    }

    @PatchMapping(path = "/{garage-id}/update/{id}")
    public BaseResponse<Long> updateEmployee(@PathVariable("garage-id") Long garageId,
                                             @PathVariable(name = "id") Long id,
                                             @RequestBody EmployeeRequest request) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(employeeService.updateEmployee(id, request, garageId));
        return response;
    }

    @DeleteMapping
    public BaseResponse<String> deleteList(@PathVariable("garage-id") Long garageId,
                                           @RequestBody List<Long> ids) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(employeeService.deleteList(ids));
        return response;
    }
}
