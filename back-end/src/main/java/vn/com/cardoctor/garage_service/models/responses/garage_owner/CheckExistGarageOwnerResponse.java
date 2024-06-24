package vn.com.cardoctor.garage_service.models.responses.garage_owner;

import lombok.Data;

@Data
public class CheckExistGarageOwnerResponse {
    private String phoneNumber;
    private Boolean isExist;
}
