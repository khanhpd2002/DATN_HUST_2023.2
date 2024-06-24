package vn.com.cardoctor.garage_service.services.garages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.cardoctor.garage_service.entities.garages.GarageServiceType;
import vn.com.cardoctor.garage_service.repositories.garages.GarageServiceTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GarageServiceTypeService {
    private final GarageServiceTypeRepository garageServiceTypeRepository;

    public List<GarageServiceType> findAllByGarageId(Long garageId) {
        return this.garageServiceTypeRepository.findByGarageId(garageId);
    }

    public List<GarageServiceType> findServiceTypeApplyForAll() {
        return this.garageServiceTypeRepository.findServiceTypeApplyForAll();
    }
}
