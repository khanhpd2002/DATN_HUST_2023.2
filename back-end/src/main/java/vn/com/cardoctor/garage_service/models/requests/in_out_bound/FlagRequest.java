package vn.com.cardoctor.garage_service.models.requests.in_out_bound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlagRequest {
    private Boolean isUpdate;
    private Boolean isCreateNewOutbound;
    private Boolean isSellSparePart;
}
