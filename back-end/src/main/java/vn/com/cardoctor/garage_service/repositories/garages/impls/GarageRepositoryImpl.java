package vn.com.cardoctor.garage_service.repositories.garages.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.dtos.GarageInfoDto;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageBookingRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageInfoResponse;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Log4j2
public class GarageRepositoryImpl implements GarageRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllGarage(String code, String name, Long provinceId, Long districtId, Long wardId, String address,
                                            String contactPointName, String contactPointPhone,
                                            Integer status, List<Long> carSubSystems, List<Long> rescues, List<Long> insurances, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("select g.* from garages g ");
        if (Objects.nonNull(carSubSystems)) {
            jpql.append("   join garage_car_sub_systems gcss on gcss.car_sub_system_id in :carSubSystems ");
            params.put("carSubSystems", carSubSystems);
        }
        if (Objects.nonNull(rescues)) {
            jpql.append("   join garage_rescues gr on gr.rescue_id in :rescues ");
            params.put("rescues", rescues);
        }
        if (Objects.nonNull(insurances)) {
            jpql.append("   join garage_insurances gi on gi.insurance_id in :insurances ");
            params.put("insurances", insurances);
        }
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(status)) {
            params.put("status", status);
            jpql.append("and g.status = :status ");
        }
        if (Objects.nonNull(name)) {
            params.put("name", "%" + name + "%");
            jpql.append("and g.name like :name ");
        }
        if (Objects.nonNull(contactPointName)) {
            params.put("contactPointName", "%" + contactPointName + "%");
            jpql.append("and g.contact_point_name like :contactPointName ");
        }
        if (Objects.nonNull(contactPointPhone)) {
            params.put("contactPointPhone", "%" + contactPointPhone + "%");
            jpql.append("and g.contact_point_phone like :contactPointPhone ");
        }
        if (Objects.nonNull(code)) {
            params.put("code", "%" + code + "%");
            jpql.append("and g.code like :code) ");
        }
        if (Objects.nonNull(provinceId)) {
            params.put("provinceId", provinceId);
            jpql.append("and g.province_id = :provinceId ");
        }
        if (Objects.nonNull(districtId)) {
            params.put("districtId", districtId);
            jpql.append("and g.district_id = :districtId ");
        }
        if (Objects.nonNull(wardId)) {
            params.put("wardId", wardId);
            jpql.append("and g.ward_id = :wardId ");
        }
        if (Objects.nonNull(address)) {
            params.put("address", "%" + address + "%");
            jpql.append("and g.address like :address ");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by g.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), Garage.class);
        params.forEach(q::setParameter);
        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public PagingDataResponse searchGarageToBooking(GarageBookingRequest garageBookingRequest, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("select g.*, haversine_distance(:currentLat, :currentLng, g.latitude, g.longitude) as distance from garages g ");
        jpql.append("   join services s on s.id is not null ");
        jpql.append("   join garage_car_sub_systems gcss on g.id = gcss.garage_id ");
        jpql.append("   join service_car_sub_systems scss on s.id = scss.service_id and s.id in (:services) ");
        jpql.append("       and gcss.car_sub_system_id = scss.car_sub_system_id ");
        jpql.append("           where haversine_distance(:currentLat, :currentLng, g.latitude, g.longitude) < :maxDistance ");

        params.put("services", garageBookingRequest.getServiceIds());
        params.put("currentLat", garageBookingRequest.getCurrentLat());
        params.put("currentLng", garageBookingRequest.getCurrentLng());
        params.put("maxDistance", garageBookingRequest.getMaxDistance());

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" group by g.id order by distance, g.is_verified desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), Garage.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public GarageInfoResponse findGarageInfoById(Long id) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select g.id, g.contact_point_phone, g.contact_point_name, g.name, g.address, g.contact_point_email, gg.tax_code, g.logo, g.additional_information from garages g\n" +
                "    join garage_groups gg on gg.id = g.garage_group_id\n" +
                "    where g.id = :id ");
        params.put("id", id);
        Query q = entityManager.createNativeQuery(jpql.toString(), "GarageInfoDto");
        params.forEach(q::setParameter);
        GarageInfoResponse response = new GarageInfoResponse();
        GarageInfoDto garageInfoDto = (GarageInfoDto) q.getSingleResult();
        response.setId(garageInfoDto.getId());
        response.setContractPointPhone(garageInfoDto.getContractPointPhone());
        response.setContactPointName(garageInfoDto.getContactPointName());
        response.setName(garageInfoDto.getName());
        response.setAddress(garageInfoDto.getAddress());
        response.setContactPointEmail(garageInfoDto.getContactPointEmail());
        response.setTaxCode(garageInfoDto.getTaxCode());
        response.setLogo(garageInfoDto.getLogo());
        Map<String, Object> additionalInformation = new HashMap<>();
        try {
            additionalInformation = new ObjectMapper().readValue(
                    garageInfoDto.getAdditionalInformation() != null ? garageInfoDto.getAdditionalInformation() : "{}", HashMap.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        response.setAdditionalInformation(additionalInformation);
        return response;
    }

    @Override
    public void insertGarage(GarageRequest garageRequest) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" insert into garages (id, name, contact_point_name, contact_point_phone, avatar, latitude, longitude, ");
        jpql.append(" province_id, district_id, ward_id, address, parent_garage_id, open_time, close_time, ");
        jpql.append(" rating, is_verified, status, created_at, updated_at) ");
        jpql.append(" values (:id, :name, :contact_point_name, :contact_point_phone, :avatar, :latitude, :longitude, ");
        jpql.append(" :province_id, :district_id, :ward_id, :address, :parent_garage_id, :open_time, :close_time, ");
        jpql.append(" :rating, :is_verified, :status, :created_at, :updated_at) ");

        params.put("id", garageRequest.getId());
        params.put("name", garageRequest.getName());
        params.put("contact_point_name", garageRequest.getContactPointName());
        params.put("contact_point_phone", garageRequest.getContactPointPhone());
        params.put("avatar", garageRequest.getAvatar());
        params.put("latitude", garageRequest.getLatitude());
        params.put("longitude", garageRequest.getLongitude());
        params.put("province_id", garageRequest.getProvinceId());
        params.put("district_id", garageRequest.getDistrictId());
        params.put("ward_id", garageRequest.getWardId());
        params.put("address", garageRequest.getAddress());
//        params.put("parent_garage_id", garageRequest.getParentGarageId());
//        params.put("open_time", garageRequest.getOpenTime());
//        params.put("close_time", garageRequest.getCloseTime());
        params.put("rating", null);
        params.put("is_verified", garageRequest.getIsVerified());
        params.put("status", EntityStatus.ACCEPTED.getCode());
        params.put("created_at", new Date());
        params.put("updated_at", new Date());
        Query q = entityManager.createNativeQuery(jpql.toString());
        params.forEach(q::setParameter);
        q.executeUpdate();
    }
}
