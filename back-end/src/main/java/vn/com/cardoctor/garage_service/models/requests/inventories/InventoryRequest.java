package vn.com.cardoctor.garage_service.models.requests.inventories;

import lombok.Data;

@Data
public class InventoryRequest {
    private String name;
    private String description;
}
