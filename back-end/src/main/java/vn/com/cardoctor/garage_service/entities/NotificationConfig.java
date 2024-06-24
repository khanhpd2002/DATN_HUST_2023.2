package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "notification_configs")
public class NotificationConfig extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String notification;
    private String title;
    private String body;
    private String image;
}
