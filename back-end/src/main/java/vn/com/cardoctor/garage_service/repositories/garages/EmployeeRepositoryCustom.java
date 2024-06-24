package vn.com.cardoctor.garage_service.repositories.garages;

import model.PagingDataResponse;

import java.util.Date;

public interface EmployeeRepositoryCustom {
    PagingDataResponse searchEmployee(Long garageId, String fullName, Integer employeeType, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize);

    Boolean isDuplicateName(String name, Long employeeId, Long garageId);
}
