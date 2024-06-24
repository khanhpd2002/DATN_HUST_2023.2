package vn.com.cardoctor.garage_service.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDto {
    private String url;
    private Boolean deleted;
}

