package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "access_tokens")
public class AccessToken extends BaseEntity {

    @Id
    private String token;
    private Long userId;
    private String username;
    private Long garageId;
    private Long expireTime;
}
