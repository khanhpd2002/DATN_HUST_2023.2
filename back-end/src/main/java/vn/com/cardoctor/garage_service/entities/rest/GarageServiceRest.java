package vn.com.cardoctor.garage_service.entities.rest;

import lombok.Data;

@Data
public class GarageServiceRest {
    private String name;
    private String description;
    private Integer status;
    private String what;
    private String how;
    private String recommend;
    private String keepInMind;
    private String important;
}
