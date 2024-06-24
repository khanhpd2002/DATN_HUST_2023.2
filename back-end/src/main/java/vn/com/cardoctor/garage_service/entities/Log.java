package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
@Table(name = "logs")
public class Log extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String params;
    @Type(type = "json")
    private String request;
    @Type(type = "json")
    private String response;
    @Type(type = "json")
    private String additionalInformation;
}
