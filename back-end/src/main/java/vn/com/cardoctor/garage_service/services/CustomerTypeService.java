package vn.com.cardoctor.garage_service.services;

import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.CustomerType;
import vn.com.cardoctor.garage_service.models.requests.customer.CustomerTypeRequest;
import vn.com.cardoctor.garage_service.repositories.CustomerTypeRepository;

import java.util.Optional;

@Service
public class CustomerTypeService {
    @Autowired
    CustomerTypeRepository customerTypeRepository;

    public PagingDataResponse findAllCustomerTypeForAll(String name, Integer pageSize, Integer pageNumber) {
        return this.customerTypeRepository.findAll(name, pageSize, pageNumber);
    }

    public PagingDataResponse findAllCustomerType(Long garageId, String name, Integer pageSize, Integer pageNumber) {
        return this.customerTypeRepository.findAllByGarageId(garageId, name, pageSize, pageNumber);
    }

    public Long createForAll(CustomerTypeRequest customerTypeRequest) {
        CustomerType customerType = new CustomerType();
        customerType.setCustomerTypeName(customerTypeRequest.getCustomerTypeName());
        customerType.setDescription(customerTypeRequest.getDescription());
        customerType = this.customerTypeRepository.save(customerType);
        return customerType.getId();
    }

    public Long create(Long garageId, CustomerTypeRequest customerTypeRequest) {
        CustomerType customerType = new CustomerType();
        customerType.setCustomerTypeName(customerTypeRequest.getCustomerTypeName());
        customerType.setDescription(customerTypeRequest.getDescription());
        customerType.setGarageId(garageId);
        customerType = this.customerTypeRepository.save(customerType);
        return customerType.getId();
    }

    public Long update(Long garageId, CustomerTypeRequest customerTypeRequest, Long customerTypeId) throws ApiException {
        Optional<CustomerType> oCustomerType = this.customerTypeRepository.findById(customerTypeId);
        if (oCustomerType.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin loại khách hàng");
        }
        CustomerType customerType = oCustomerType.get();
        customerType.setCustomerTypeName(customerTypeRequest.getCustomerTypeName());
        customerType.setDescription(customerTypeRequest.getDescription());
        customerType.setGarageId(garageId);
        customerType = this.customerTypeRepository.save(customerType);
        return customerType.getId();
    }
}
