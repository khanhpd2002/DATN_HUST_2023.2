package vn.com.cardoctor.garage_service.repositories.orders;

import vn.com.cardoctor.garage_service.models.dtos.QuotationServiceDto;

import java.util.List;

public interface QuotationServiceRepositoryCustom {
    List<QuotationServiceDto> getQuotationService(Long quotationId);
}
