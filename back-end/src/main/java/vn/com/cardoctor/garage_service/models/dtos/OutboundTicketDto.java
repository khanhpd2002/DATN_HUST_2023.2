package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboundTicketDto {
    private Long id;
    private Date date;
    private String code;
    private String ticketCode;
    private Integer type;
    private Integer status;
}
