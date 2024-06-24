package vn.com.cardoctor.garage_service.entities.orders;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;
import vn.com.cardoctor.garage_service.models.dtos.AdminRepairOrderDto;
import vn.com.cardoctor.garage_service.models.dtos.RepairOrderDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@SqlResultSetMapping(
        name = "RepairOrderDto",
        classes = {
                @ConstructorResult(
                        targetClass = RepairOrderDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "customer_id", type = Long.class),
                                @ColumnResult(name = "customer_name", type = String.class),
                                @ColumnResult(name = "car_id", type = Long.class),
                                @ColumnResult(name = "license_plate", type = String.class),
                                @ColumnResult(name = "garage_id", type = Long.class),
                                @ColumnResult(name = "garage_name", type = String.class),
                                @ColumnResult(name = "customer_type_name", type = String.class),
                                @ColumnResult(name = "appointment_date", type = Date.class),
                                @ColumnResult(name = "status", type = Integer.class),
                                @ColumnResult(name = "payment_status", type = Integer.class),
                                @ColumnResult(name = "total_price", type = BigDecimal.class),
                        })})

@SqlResultSetMapping(
        name = "AdminRepairOrderDto",
        classes = {
                @ConstructorResult(
                        targetClass = AdminRepairOrderDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "garage_id", type = Long.class),
                                @ColumnResult(name = "garage_name", type = String.class),
                                @ColumnResult(name = "garage_owner_phone", type = String.class),
                                @ColumnResult(name = "appointment_date", type = Date.class),
                                @ColumnResult(name = "total_price", type = BigDecimal.class),
                                @ColumnResult(name = "status", type = Integer.class),
                        })})

@Entity
@Data
@Table(name = "repair_orders")
public class RepairOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private long garageId;
    private long customerId;
    private long carId;
    private String services;
    private Integer priority;
    private Date appointmentDate;
    private Integer timeFrame;
    private Integer odo;
    private Long expertId;
    private Long operatorId;
    private String description;
    private String image;
    private Integer status;
    private Integer paymentStatus;
    private LocalDateTime expectedHandoverDate;
    private String note;
    private Date canceledAt;
    private String createdBy;
}
