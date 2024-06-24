package vn.com.cardoctor.garage_service.entities.dashboards;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Datasets {
    private String label;
    private List<BigDecimal> data;
    private String backgroundColor;
    private String borderColor;
    private String type;
    private Integer order;
}
