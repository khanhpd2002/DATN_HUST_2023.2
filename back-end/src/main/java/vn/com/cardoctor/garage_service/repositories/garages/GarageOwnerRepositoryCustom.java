package vn.com.cardoctor.garage_service.repositories.garages;

import model.BaseResponse;
import model.PagingDataResponse;

import java.util.Date;

public interface GarageOwnerRepositoryCustom {
    PagingDataResponse findAllGarageOwner(String name, String userName, String phone, String email, Integer gender,
                                          Date fromDateBirthday, Date toDateBirthday, Integer status, Integer pageSize, Integer pageNumber);
}
