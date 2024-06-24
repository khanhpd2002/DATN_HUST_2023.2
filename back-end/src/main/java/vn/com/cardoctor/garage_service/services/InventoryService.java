package vn.com.cardoctor.garage_service.services;

import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.Inventory;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryRequest;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;
import vn.com.cardoctor.garage_service.repositories.InventoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    GarageRepository garageRepository;

    public Long create(InventoryRequest inventoryRequest, Long garageId) throws ApiException {
        this.validRequest(inventoryRequest);
        Inventory inventory = new Inventory();
        this.convertRequestToEntity(inventory, inventoryRequest);
        inventory.setGarageId(garageId);
        inventory = this.inventoryRepository.save(inventory);
        return inventory.getId();
    }

    public PagingDataResponse findAllInventory(Long garageId, String name, Integer pageSize, Integer pageNumber) {
        return this.inventoryRepository.findAllInventory(garageId, name, pageSize, pageNumber);
    }

    public String createInventoryForAllGarage() {
        List<Garage> garages = this.garageRepository.findAll();
        for (Garage garage : garages) {
            Optional<Inventory> oInventory = this.inventoryRepository.findFirstByGarageId(garage.getId());
            if (oInventory.isEmpty()) {
                Inventory inventory = new Inventory();
                inventory.setGarageId(garage.getId());
                inventory.setName("Kho của " + garage.getName());
                inventory.setDescription("Kho của " + garage.getName());
                this.inventoryRepository.save(inventory);
            }
        }
        return "Success";
    }

    public String createInventoryForGarageFrom(Long garageId) {
        List<Garage> garages = this.garageRepository.findAllGarageFromId(garageId);
        for (Garage garage : garages) {
            Optional<Inventory> oInventory = this.inventoryRepository.findFirstByGarageId(garage.getId());
            if (oInventory.isEmpty()) {
                Inventory inventory = new Inventory();
                inventory.setGarageId(garage.getId());
                inventory.setName("Kho của " + garage.getName());
                inventory.setDescription("Kho của " + garage.getName());
                this.inventoryRepository.save(inventory);
            }
        }
        return "Success";
    }

    public void convertRequestToEntity(Inventory inventory, InventoryRequest inventoryRequest) {
        inventory.setName(inventoryRequest.getName());
        inventory.setDescription(inventoryRequest.getDescription());
    }

    public void validRequest(InventoryRequest inventoryRequest) throws ApiException {
        if (inventoryRequest.getName() == null || inventoryRequest.getName() == "") {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin tên kho chứa");
        }
    }

}
