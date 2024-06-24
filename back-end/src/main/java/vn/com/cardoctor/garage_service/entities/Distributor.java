package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.DistributorDto;
import vn.com.cardoctor.garage_service.models.dtos.ProductDto;

import javax.persistence.*;
import java.math.BigDecimal;

@SqlResultSetMapping(
        name = "DistributorDto",
        classes = {
                @ConstructorResult(
                        targetClass = DistributorDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "province_id", type = Long.class),
                                @ColumnResult(name = "province_name", type = String.class),
                                @ColumnResult(name = "district_id", type = Long.class),
                                @ColumnResult(name = "district_name", type = String.class),
                                @ColumnResult(name = "ward_id", type = Long.class),
                                @ColumnResult(name = "ward_name", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                                @ColumnResult(name = "contact_name", type = String.class),
                                @ColumnResult(name = "contact_phone", type = String.class),
                                @ColumnResult(name = "garage_id", type = Long.class),
                                @ColumnResult(name = "garage_name", type = String.class),
                        })})

@Entity
@Data
@Table(name = "distributors")
public class Distributor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private String address;
    private String contactName;
    private String contactPhone;
    private Long garageId;
    private Integer isDelete;
    @Transient
    private String errors;
}
