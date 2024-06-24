package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "project_configs")
public class ProjectConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String value;
}
