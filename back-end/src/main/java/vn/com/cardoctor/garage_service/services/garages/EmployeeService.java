package vn.com.cardoctor.garage_service.services.garages;

import lombok.RequiredArgsConstructor;
import model.PagingDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.Employee;
import vn.com.cardoctor.garage_service.models.requests.employee.EmployeeRequest;
import vn.com.cardoctor.garage_service.models.responses.employee.EmployeeResponse;
import vn.com.cardoctor.garage_service.repositories.garages.EmployeeRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private static final Pattern VIETNAMESE_PATTERN = Pattern.compile("^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]+$");

    public PagingDataResponse searchEmployee(Long garageId, String fullName, Integer employeeType, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        return employeeRepository.searchEmployee(garageId, fullName, employeeType, status, startDate, endDate, page, pageSize);
    }

    public EmployeeResponse getDetailEmployee(Long id) throws ApiException {
        Optional<Employee> oEmployee = this.employeeRepository.findById(id);
        if (oEmployee.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        Employee employee = oEmployee.get();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(employee.getId());
        employeeResponse.setFullName(employee.getFullName());
        employeeResponse.setGarageId(employee.getGarageId());
        employeeResponse.setType(employee.getType());
        employeeResponse.setBirthday(employee.getBirthday());
        employeeResponse.setGender(employee.getGender());
        employeeResponse.setStatus(employee.getStatus());
        employeeResponse.setContractType(employee.getContractType());
        employeeResponse.setStartDate(employee.getStartDate());
        employeeResponse.setEndDate(employee.getEndDate());
        employeeResponse.setPhoneNumber(employee.getPhoneNumber());
        return employeeResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createEmployee(Long garageId, EmployeeRequest request) throws ApiException {
        validEmployeeInfo(request, null, garageId);
        Employee employee = new Employee();
        employee.setGarageId(garageId);
        employee.setFullName(request.getFullName());
        employee.setGender(request.getGender());
        employee.setBirthday(request.getBirthday());
        employee.setType(request.getType());
        employee.setStatus(request.getStatus());
        employee.setContractType(request.getContractType());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee = employeeRepository.save(employee);
        return employee.getId();
    }

    public Long updateEmployee(Long id, EmployeeRequest request, Long garageId) throws ApiException {
        Optional<Employee> oEmployee = this.employeeRepository.findById(id);
        if (oEmployee.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        validEmployeeInfo(request, id, garageId);
        Employee employee = oEmployee.get();
        employee.setFullName(request.getFullName());
        employee.setGender(request.getGender());
        employee.setBirthday(request.getBirthday());
        employee.setType(request.getType());
        employee.setStatus(request.getStatus());
        employee.setContractType(request.getContractType());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setPhoneNumber(request.getPhoneNumber());
        employeeRepository.save(employee);
        return id;
    }

    public Long removeEmployee(Long id) throws ApiException {
        Optional<Employee> oEmployee = this.employeeRepository.findById(id);
        if (oEmployee.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        employeeRepository.deteleById(id);
        return id;
    }

    public String deleteList(List<Long> ids) {
        List<Employee> employees = this.employeeRepository.findByListId(ids);
        for (Employee employee : employees) {
            employee.setStatus(1);
        }
        this.employeeRepository.saveAll(employees);
        return "Success";
    }

    private void validEmployeeInfo(EmployeeRequest employee, Long employeeId, Long garageId) throws ApiException {
        if (Objects.isNull(employee)) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin cá nhân không được để trống");
        }
        if (!StringUtils.hasText(employee.getFullName())) {
            throw new ApiException(ERROR.BAD_REQUEST, "Họ và tên không được để trống");
        } else {
            if (!VIETNAMESE_PATTERN.matcher(employee.getFullName()).matches()) {
                throw new ApiException(ERROR.BAD_REQUEST, "Họ và tên không đúng định dạng");
            }
        }
        if (!StringUtils.hasText(employee.getPhoneNumber())) {
            throw new ApiException(ERROR.BAD_REQUEST, "SĐT không được để trống");
        }
    }

    public BigDecimal getEmployeeCountByGarage(Long garageId) {
        return employeeRepository.getEmployeeCountByGarage(garageId);
    }
}
