package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.GarageContractDto;
import vn.com.cardoctor.garage_service.models.dtos.OutboundTicketDto;

import javax.persistence.*;
import java.util.Date;

//@SqlResultSetMapping(
//        name = "OutboundTicketDto",
//        classes = {
//                @ConstructorResult(
//                        targetClass = OutboundTicketDto.class,
//                        columns = {
//                                @ColumnResult(name = "id", type = Long.class),
//                                @ColumnResult(name = "date", type = Date.class),
//                                @ColumnResult(name = "code", type = String.class),
//                                @ColumnResult(name = "ticket_code", type = String.class),
//                                @ColumnResult(name = "type", type = Integer.class),
//                                @ColumnResult(name = "status", type = Integer.class),
//                        })})

@Entity
@Data
@Table(name = "outbound_tickets")
public class OutboundTicket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Integer type;
    private Long distributorId;
    private Long ticketId;
    private String products;
    private String note;
    private Integer status;
    private Long garageId;
}
