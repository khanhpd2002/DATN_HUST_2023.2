package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;
import vn.com.cardoctor.garage_service.models.dtos.GarageContractDto;
import vn.com.cardoctor.garage_service.models.dtos.ProductDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SqlResultSetMapping(
        name = "GarageContractDto",
        classes = {
                @ConstructorResult(
                        targetClass = GarageContractDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "contract_number", type = String.class),
                                @ColumnResult(name = "garage_id", type = Long.class),
                                @ColumnResult(name = "contract_from_date", type = Date.class),
                                @ColumnResult(name = "contract_to_date", type = Date.class),
                                @ColumnResult(name = "document", type = String.class),
                                @ColumnResult(name = "status", type = Integer.class),
                                @ColumnResult(name = "garage_name", type = String.class),
                        })})

@Entity
@Data
@Table(name = "garage_contracts")
public class GarageContract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String contractNumber;
    private Long garageId;
    @Temporal(TemporalType.DATE)
    private Date contractFromDate;
    @Temporal(TemporalType.DATE)
    private Date contractToDate;
    private String document;
    private Integer status;
}
