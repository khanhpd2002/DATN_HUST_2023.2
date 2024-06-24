package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "inbound_tickets")
public class InboundTicket extends BaseEntity {
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
