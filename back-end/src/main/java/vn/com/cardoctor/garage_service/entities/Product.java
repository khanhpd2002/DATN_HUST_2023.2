package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.ProductDto;
import vn.com.cardoctor.garage_service.models.dtos.ProductHistoryDto;
import vn.com.cardoctor.garage_service.models.dtos.ProductRefundedDto;
import vn.com.cardoctor.garage_service.models.dtos.QuotationProductDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SqlResultSetMapping(
        name = "ProductDto",
        classes = {
                @ConstructorResult(
                        targetClass = ProductDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "parent_product_id", type = Long.class),
                                @ColumnResult(name = "spare_part_type", type = Integer.class),
                                @ColumnResult(name = "distributor_id", type = Long.class),
                                @ColumnResult(name = "distributor_code", type = String.class),
                                @ColumnResult(name = "distributor_name", type = String.class),
                                @ColumnResult(name = "purchase_price", type = BigDecimal.class),
                                @ColumnResult(name = "quantity", type = BigDecimal.class),
                                @ColumnResult(name = "unit", type = String.class),
                        })})

@SqlResultSetMapping(
        name = "QuotationProductDto",
        classes = {
                @ConstructorResult(
                        targetClass = QuotationProductDto.class,
                        columns = {
                                @ColumnResult(name = "quotation_id", type = Long.class),
                                @ColumnResult(name = "garage_service_id", type = Long.class),
                                @ColumnResult(name = "garage_service_type_id", type = Long.class),
                                @ColumnResult(name = "garage_service_type_name", type = String.class),
                                @ColumnResult(name = "product_id", type = Long.class),
                                @ColumnResult(name = "product_code", type = String.class),
                                @ColumnResult(name = "product_name", type = String.class),
                                @ColumnResult(name = "quantity", type = BigDecimal.class),
                                @ColumnResult(name = "unit_price", type = BigDecimal.class),
                                @ColumnResult(name = "unit", type = String.class),
                                @ColumnResult(name = "origin_price", type = BigDecimal.class),
                                @ColumnResult(name = "discount", type = BigDecimal.class),
                                @ColumnResult(name = "tax", type = BigDecimal.class),
                                @ColumnResult(name = "price", type = BigDecimal.class),
                                @ColumnResult(name = "employee_id", type = Long.class),
                                @ColumnResult(name = "employee_name", type = String.class),
                                @ColumnResult(name = "product_type", type = Integer.class),
                                @ColumnResult(name = "status", type = Integer.class),
                                @ColumnResult(name = "outbound_product_id", type = Long.class),
                        })})

@SqlResultSetMapping(
        name = "ProductRefundedDto",
        classes = {
                @ConstructorResult(
                        targetClass = ProductRefundedDto.class,
                        columns = {
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "refund_quantity", type = BigDecimal.class),
                        })})

@SqlResultSetMapping(
        name = "ProductHistoryDto",
        classes = {
                @ConstructorResult(
                        targetClass = ProductHistoryDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "unit", type = String.class),
                                @ColumnResult(name = "reality_unit_price", type = BigDecimal.class),
                                @ColumnResult(name = "system_unit_price", type = BigDecimal.class),
                                @ColumnResult(name = "quantity", type = BigDecimal.class),
                        })})

@Entity
@Data
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private Long parentProductId;
    private Integer sparePartType;
    private Long distributorId;
    private BigDecimal purchasePrice;
    private BigDecimal quantity;
    private String unit;
    private Long inventoryId;
    private Long garageId;
    private Date firstSellAt;

    @Transient
    private String errors;
}
