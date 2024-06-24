package vn.com.cardoctor.garage_service.entities.dashboards;

import lombok.Data;

import java.util.List;

@Data
public class DataChart {
    private List<String> labels;
    private List<Datasets> datasets;
}
