package vn.com.cardoctor.garage_service.repositories.external;

import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.models.dtos.CarInfoDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CarInfoRepositoryImpl {
    @PersistenceContext
    EntityManager entityManager;

    public List<CarInfoDto> findAllCarInfo() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT cb.id as car_brand_id, cb.title as car_brand, cm.id as car_model_id, cm.title as car_model, cy.id as car_year_id, cy.title as car_year, cv.id as car_version_id, cv.title as car_version ");
        jpql.append(" FROM `cardoctor_cms`.car_brands cb ");
        jpql.append(" JOIN `cardoctor_cms`.car_models cm on cb.id = cm.car_brand_id ");
        jpql.append(" JOIN `cardoctor_cms`.car_years cy on cm.id = cy.car_model_id ");
        jpql.append(" JOIN `cardoctor_cms`.car_versions cv on cy.id = cv.car_year_id ");
        jpql.append(" ORDER BY car_brand_id, car_model_id, car_year_id, car_version_id ");
        Query query = this.entityManager.createNativeQuery(jpql.toString());
        List<Object[]> result = query.getResultList();
        List<CarInfoDto> carInfoDtos = new ArrayList<>();
        for (Object[] row : result) {
            CarInfoDto carInfoDto = new CarInfoDto();
            carInfoDto.setCarBrandId(((BigInteger) row[0]).longValue());
            carInfoDto.setCarBrand((String) row[1]);
            carInfoDto.setCarModelId(((BigInteger) row[2]).longValue());
            carInfoDto.setCarModel((String) row[3]);
            carInfoDto.setCarYearId(((BigInteger) row[4]).longValue());
            carInfoDto.setCarYear((String) row[5]);
            carInfoDto.setCarVersionId(((BigInteger) row[6]).longValue());
            carInfoDto.setCarVersion((String) row[7]);
            carInfoDtos.add(carInfoDto);
        }
        return carInfoDtos;
    }
}
