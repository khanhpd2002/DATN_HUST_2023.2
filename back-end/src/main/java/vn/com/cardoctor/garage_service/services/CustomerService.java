package vn.com.cardoctor.garage_service.services;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.garages.GarageService;
import vn.com.cardoctor.garage_service.enums.ProductTypeEnum;
import vn.com.cardoctor.garage_service.models.dtos.CarDto;
import vn.com.cardoctor.garage_service.models.requests.customer.CustomerRequest;
import vn.com.cardoctor.garage_service.models.responses.customer.CustomerResponse;
import vn.com.cardoctor.garage_service.repositories.*;
import vn.com.cardoctor.garage_service.repositories.garages.GarageServiceRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerCarRepository customerCarRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CustomerTypeRepository customerTypeRepository;

    @Autowired
    ConfigPriceRepository configPriceRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    GarageServiceRepository garageServiceRepository;

    public PagingDataResponse findAllCustomer(Long garageId, Long customerTypeId, String customerName, String phoneNumber,
                                              Integer pageSize, Integer pageNumber) {
        return this.customerRepository.findAllCustomer(garageId, customerTypeId, customerName, phoneNumber, pageSize, pageNumber);
    }

    public BaseResponse<CustomerResponse> findById(Long garageId, Long customerId) throws ApiException {
        Optional<Customer> oCustomer = this.customerRepository.findById(customerId);
        if (oCustomer.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin khách hàng");
        }
        Customer customer = oCustomer.get();
        List<CarDto> cars = this.carRepository.findCarDtoByCustomerId(customerId);
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getId());
        customerResponse.setCustomerTypeId(customer.getCustomerTypeId());
        customerResponse.setFullName(customer.getFullName());
        customerResponse.setPhoneNumber(customer.getPhoneNumber());
        customerResponse.setAddress(customer.getAddress());
        customerResponse.setGarageId(customer.getGarageId());
        customerResponse.setDriverId(customer.getDriverId());
        customerResponse.setCars(cars);
        return new BaseResponse<>(1, "Success", customerResponse);
    }

    public Long create(CustomerRequest customerRequest, Long garageId) throws ApiException {
        Customer customer = new Customer();
        this.validRequest(customerRequest, garageId);
        this.convertRequestToEntity(customer, customerRequest);
        if (Objects.isNull(customerRequest.getCustomerTypeId())) {
            CustomerType customerType = new CustomerType();
            customerType.setGarageId(garageId);
            customerType.setCustomerTypeName(customerRequest.getCustomerTypeName());
            customerType.setDescription(customerRequest.getCustomerTypeName());
            customerType = this.customerTypeRepository.save(customerType);
            customer.setCustomerTypeId(customerType.getId());
            List<Product> products = this.productRepository.findAllByGarageId(garageId);
            List<GarageService> garageServices = this.garageServiceRepository.findAllByGarageId(garageId);
            List<ConfigPrice> configPrices = new ArrayList<>();
            for (Product product : products) {
                ConfigPrice configPrice;
                configPrice = new ConfigPrice();
                configPrice.setCustomerTypeId(customerType.getId());
                configPrice.setType(ProductTypeEnum.SPARE_PART.getCode());
                configPrice.setProductId(product.getId());
                configPrice.setPrice(new BigDecimal(0));
                configPrice.setGarageId(garageId);
                configPrices.add(configPrice);
            }
            for (GarageService garageService : garageServices) {
                ConfigPrice configPrice;
                configPrice = new ConfigPrice();
                configPrice.setCustomerTypeId(customerType.getId());
                configPrice.setType(ProductTypeEnum.SERVICE.getCode());
                configPrice.setGarageServiceId(garageService.getId());
                configPrice.setPrice(new BigDecimal(0));
                configPrice.setGarageId(garageId);
                configPrices.add(configPrice);
            }
            this.configPriceRepository.saveAll(configPrices);
        }
        customer.setGarageId(garageId);
        customer = this.customerRepository.save(customer);
        return customer.getId();
    }

    public Long update(CustomerRequest customerRequest, Long garageId, Long customerId) throws ApiException {
        if (customerRequest.getPhoneNumber() == null || customerRequest.getPhoneNumber().isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin số điện thoại");
        }
        Optional<Customer> oCustomer = this.customerRepository.findById(customerId);
        if (oCustomer.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin khách hàng");
        }
        Customer customer = oCustomer.get();
        this.convertRequestToEntity(customer, customerRequest);
        customer.setGarageId(garageId);
        this.customerRepository.save(customer);
        return customerId;
    }

    public void convertRequestToEntity(Customer customer, CustomerRequest customerRequest) {
        customer.setCustomerTypeId(customerRequest.getCustomerTypeId());
        customer.setFullName(customerRequest.getFullName());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        customer.setAddress(customerRequest.getAddress());
        customer.setDriverId(customerRequest.getDriverId());
    }

    public void validRequest(CustomerRequest customerRequest, Long garageId) throws ApiException {
        if (customerRequest.getPhoneNumber() == null || customerRequest.getPhoneNumber().isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin số điện thoại");
        }
        Optional<Customer> oCustomer = this.customerRepository.findFirstByPhoneNumberAndGarageId(customerRequest.getPhoneNumber(), garageId);
        if (oCustomer.isPresent()) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Đã tồn tại thông tin khách hàng với SĐT này!");
        }
        if (customerRequest.getCustomerTypeId() == null && customerRequest.getCustomerTypeName().isEmpty()) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Thiếu thông tin nhóm khách hàng");
        }
    }
}
