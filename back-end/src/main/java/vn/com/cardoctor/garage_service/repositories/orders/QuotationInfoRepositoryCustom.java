package vn.com.cardoctor.garage_service.repositories.orders;

import vn.com.cardoctor.garage_service.models.dtos.QuotationProductDto;

import java.util.List;

public interface QuotationInfoRepositoryCustom {
    List<QuotationProductDto> findAllQuotationInfo(Long quotationId);
}
