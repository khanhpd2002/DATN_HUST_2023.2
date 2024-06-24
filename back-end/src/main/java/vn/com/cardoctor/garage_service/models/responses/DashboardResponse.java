package vn.com.cardoctor.garage_service.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardResponse {

    public List<TypeOverview> overview;

    @JsonProperty("popular_services")
    private List<PopularService> popularService;

    @JsonProperty("excellent_staffs")
    private List<ExcellentStaff> excellentStaff;

    @Data
    public static class TypeOverview {
        private String name;
        private BigDecimal current;
        private BigDecimal last;
        private String percent;
    }

    @Data
    public static class PopularService {
        private String name;
        private BigDecimal money;
    }

    @Data
    public static class ExcellentStaff {
        private String name;

        @JsonProperty("job_title")
        private String jobTitle;

        private BigDecimal money;
    }
}
