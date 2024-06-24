package vn.com.cardoctor.garage_service.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class TypeServicePopularResponse {
    private int total;

    @JsonProperty("list_type_popular_services")
    private List<ListTypeServicePopularResponse> listTypeServicePopulars;

    @Data
    public static class ListTypeServicePopularResponse {
        private BigInteger id;
        private String name;
        private BigInteger quantity;
    }
}
