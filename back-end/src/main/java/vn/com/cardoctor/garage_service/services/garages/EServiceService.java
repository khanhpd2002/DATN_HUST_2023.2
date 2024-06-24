package vn.com.cardoctor.garage_service.services.garages;

import authentication.UserAuthentication;
import authentication.UserInfoContext;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.garages.*;
import vn.com.cardoctor.garage_service.entities.garages.EService;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.garage_service.EServiceRequest;
import vn.com.cardoctor.garage_service.models.responses.e_service.EServiceResponse;
import vn.com.cardoctor.garage_service.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.repositories.garages.CarSubSystemRepository;
import vn.com.cardoctor.garage_service.repositories.garages.EServiceRepository;
import vn.com.cardoctor.garage_service.repositories.garages.ServiceTagRepository;
import vn.com.cardoctor.garage_service.repositories.garages.TagRepository;
import vn.com.cardoctor.garage_service.repositories.orders.ServiceCarSubSystemRepository;
import vn.com.cardoctor.garage_service.services.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static vn.com.cardoctor.garage_service.aop.LogAspect.USER_INFO;

@Service
public class EServiceService extends BaseService {
    @Autowired
    EServiceRepository eServiceRepository;

    @Autowired
    ServiceHistoryRepository serviceHistoryRepository;

    @Autowired
    ServiceCarSubSystemRepository serviceCarSubSystemRepository;

    @Autowired
    ServiceTagRepository serviceTagRepository;

    @Autowired
    CarSubSystemRepository carSubSystemRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${base.url}")
    String baseUrl;

    public PagingDataResponse getAllEService(String name, String description, Integer status, Integer pageSize, Integer pageNumber) throws ApiException {
        return this.eServiceRepository.getAllEService(name, description, status, pageSize, pageNumber);
    }

    public EServiceResponse getEService(Long serviceId) throws ApiException {
        Optional<EService> oEService = eServiceRepository.findById(serviceId);
        if (oEService.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        EService eService = oEService.get();
        List<ServiceCarSubSystem> serviceCarSubSystems = this.serviceCarSubSystemRepository.findAllByServiceId(serviceId);
        List<ServiceTag> serviceTags = this.serviceTagRepository.findAllByServiceId(serviceId);
        EServiceResponse eServiceResponse = new EServiceResponse();
        eServiceResponse.setId(eService.getId());
        eServiceResponse.setName(eService.getName());
        eServiceResponse.setDescription(eService.getDescription());
        eServiceResponse.setStatus(eService.getStatus());
        eServiceResponse.setIsDefault(eService.getIsDefault());
        List<CarSubSystem> carSubSystems = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        for (ServiceCarSubSystem serviceCarSubSystem : serviceCarSubSystems) {
            Optional<CarSubSystem> oCarSubSystem = this.carSubSystemRepository.findById(serviceCarSubSystem.getCarSubSystemId());
            if (oCarSubSystem.isPresent())  {
                CarSubSystem carSubSystem = oCarSubSystem.get();
                carSubSystems.add(carSubSystem);
            }
        }
        for (ServiceTag serviceTag : serviceTags) {
            Optional<Tag> oTag = this.tagRepository.findById(serviceTag.getTagId());
            if (oTag.isPresent()) {
                Tag tag = oTag.get();
                tags.add(tag);
            }
        }
        eServiceResponse.setCarSubSystems(carSubSystems);
        eServiceResponse.setTags(tags);
        return eServiceResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createEService(EServiceRequest eServiceRequest) throws ApiException {
        if (eServiceRequest.getName() == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu tên dịch vụ");
        }
        EService eService = new EService();
        eService.setName(eServiceRequest.getName());
        eService.setDescription(eServiceRequest.getDescription());
        eService.setIsDefault(eServiceRequest.getIsDefault());
        eService.setStatus(EntityStatus.PENDING.getCode());
        eService.setActiveVersion(1);
        eService = eServiceRepository.save(eService);

        List<ServiceCarSubSystem> listServiceCarSubSystem = new ArrayList<>();
        List<ServiceTag> listServiceTag = new ArrayList<>();
        for (CarSubSystem carSubSystem : eServiceRequest.getCarSubSystems()) {
            ServiceCarSubSystem serviceCarSubSystem = new ServiceCarSubSystem();
            serviceCarSubSystem.setServiceId(eService.getId());
            serviceCarSubSystem.setCarSubSystemId(carSubSystem.getId());
            listServiceCarSubSystem.add(serviceCarSubSystem);
        }
        for (Tag tag : eServiceRequest.getTags()) {
            ServiceTag serviceTag = new ServiceTag();
            serviceTag.setServiceId(eService.getId());
            serviceTag.setTagId(tag.getId());
            listServiceTag.add(serviceTag);
        }
        this.serviceCarSubSystemRepository.saveAll(listServiceCarSubSystem);
        this.serviceTagRepository.saveAll(listServiceTag);

        return eService.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateEService(Long serviceId, EServiceRequest eServiceRequest, HttpServletRequest httpServletRequest) throws ApiException {
        Optional<EService> oEService = eServiceRepository.findById(serviceId);
        if (oEService.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        if (eServiceRequest.getName() == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu tên dịch vụ");
        }
        EService eService = oEService.get();
        eService.setName(eServiceRequest.getName());
        eService.setDescription(eServiceRequest.getDescription());
        eService.setIsDefault(eService.getIsDefault());
        eService.setActiveVersion(oEService.get().getActiveVersion() + 1);
        eService = this.eServiceRepository.save(eService);

        this.serviceCarSubSystemRepository.deleteAllByServiceId(serviceId);
        this.serviceTagRepository.deleteAllByServiceId(serviceId);

        List<ServiceCarSubSystem> listServiceCarSubSystem = new ArrayList<>();
        List<ServiceTag> listServiceTag = new ArrayList<>();
        for (CarSubSystem carSubSystem : eServiceRequest.getCarSubSystems()) {
            ServiceCarSubSystem serviceCarSubSystem = new ServiceCarSubSystem();
            serviceCarSubSystem.setServiceId(serviceId);
            serviceCarSubSystem.setCarSubSystemId(carSubSystem.getId());
            listServiceCarSubSystem.add(serviceCarSubSystem);
        }
        for (Tag tag : eServiceRequest.getTags()) {
            ServiceTag serviceTag = new ServiceTag();
            serviceTag.setServiceId(serviceId);
            serviceTag.setTagId(tag.getId());
            listServiceTag.add(serviceTag);
        }

        this.serviceCarSubSystemRepository.saveAll(listServiceCarSubSystem);
        this.serviceTagRepository.saveAll(listServiceTag);

        ServiceHistory serviceHistory = new ServiceHistory();
        serviceHistory.setServiceId(serviceId);
        serviceHistory.setVersion(eService.getActiveVersion());
        Map<String, Object> content = new HashMap<>();
        content.put("description", eService.getDescription());
        content.put("status", eService.getStatus());
        content.put("name", eService.getName());
        serviceHistory.setContent(content);

        UserInfoContext.add(httpServletRequest.getHeader(USER_INFO));
        UserAuthentication userAuthentication = UserInfoContext.getUserInfo();
        serviceHistory.setUpdatedBy(userAuthentication.getUserName());
        serviceHistoryRepository.save(serviceHistory);

        return serviceId;
    }

    public String removeEService(Long serviceId) throws ApiException {
        Optional<EService> oGarageService = this.eServiceRepository.findById(serviceId);
        if (oGarageService.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        eServiceRepository.deteleById(serviceId);
        return "Success";
    }

    public String acceptList(List<Long> ids) {
        List<EService> listEService = this.eServiceRepository.findByListId(ids);
        for (EService eService : listEService) {
            eService.setStatus(EntityStatus.ACCEPTED.getCode());
        }
        this.eServiceRepository.saveAll(listEService);
        return "Accept success";
    }

    public String rejectList(List<Long> ids) {
        List<EService> listEService = this.eServiceRepository.findByListId(ids);
        for (EService eService : listEService) {
            eService.setStatus(EntityStatus.REJECTED.getCode());
        }
        this.eServiceRepository.saveAll(listEService);
        return "Reject success";
    }

}
