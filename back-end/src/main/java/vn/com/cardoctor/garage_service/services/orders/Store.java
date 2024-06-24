//List<QuotationInfo> quotationInfos = new ArrayList<>();
//        // Set báo giá chi tiết nhân công
//        for (QuotationLabourRequest quotationLabour : request.getQuotationLabours()) {
//        QuotationInfo quotationInfo = new QuotationInfo();
//        quotationInfo.setQuotationId(quotation.getId());
//        quotationInfo.setType(QuotationTypeEnum.SERVICE.getCode());
//        quotationInfo.setGarageServiceId(quotationLabour.getGarageServiceId());
//        quotationInfo.setUnitPrice(quotationLabour.getUnitPrice());
//        quotationInfo.setQuantity(quotationLabour.getQuantity());
//        quotationInfo.setDiscount(quotationLabour.getDiscount());
//        quotationInfo.setTax(quotationLabour.getTax());
//        BigDecimal originPrice = quotationLabour.getUnitPrice().multiply(quotationLabour.getQuantity());
//        BigDecimal price = originPrice.subtract(quotationLabour.getDiscount()).multiply(new BigDecimal(1).add(quotationLabour.getTax()));
//        quotationInfo.setOriginPrice(originPrice);
//        quotationInfo.setPrice(price);
//        quotationInfo.setEmployeeId(quotationLabour.getEmployeeId());
//        totalPrice = totalPrice.add(price);
//        quotationInfos.add(quotationInfo);
//        }
//        // Set báo giá chi tiết vật tư, phụ tùng
//        for (QuotationSparePartGmsRequest quotationSparePart : request.getQuotationSpareParts()) {
//        QuotationInfo quotationInfo = new QuotationInfo();
//        quotationInfo.setQuotationId(quotation.getId());
//        quotationInfo.setType(QuotationTypeEnum.SPARE_PART.getCode());
//        quotationInfo.setProductId(quotationSparePart.getProductId());
//        quotationInfo.setUnitPrice(quotationSparePart.getUnitPrice());
//        quotationInfo.setQuantity(quotationSparePart.getQuantity());
//        quotationInfo.setDiscount(quotationSparePart.getDiscount());
//        quotationInfo.setTax(quotationSparePart.getTax());
//        BigDecimal originPrice = quotationSparePart.getUnitPrice().multiply(quotationSparePart.getQuantity());
//        BigDecimal price = originPrice.subtract(quotationSparePart.getDiscount()).multiply(new BigDecimal(1).add(quotationSparePart.getTax()));
//        quotationInfo.setOriginPrice(originPrice);
//        quotationInfo.setPrice(price);
//        quotationInfo.setStatus(quotationSparePart.getStatus());
//        totalPrice = totalPrice.add(price);
//        quotationInfos.add(quotationInfo);
//        }