package vn.com.cardoctor.garage_service.entities.orders;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "work_orders")
public class WorkOrder extends BaseEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long repairOrderId;
    @Temporal(TemporalType.DATE)
    private Date scheduledDate;
    @Temporal(TemporalType.DATE)
    private Date plannedEndDate;
    @Temporal(TemporalType.TIME)
    private Date plannedDuration;
    @Temporal(TemporalType.DATE)
    private Date actualStartDate;
    @Temporal(TemporalType.DATE)
    private Date actualEndDate;
    @Temporal(TemporalType.TIME)
    private Date actualDuration;
    private int status;
//    private Date createdAt;
//    private Date updatedAt;
}
