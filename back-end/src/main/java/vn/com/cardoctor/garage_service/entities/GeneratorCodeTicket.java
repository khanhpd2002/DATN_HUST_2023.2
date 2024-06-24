package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "generator_code_tickets")
public class GeneratorCodeTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Integer currentTicket;
    private String currentMonth;
    private Long garageId;
}
