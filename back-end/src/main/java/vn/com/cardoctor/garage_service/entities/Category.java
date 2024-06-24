package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import vn.com.cardoctor.garage_service.services.excels.BaseExcelService;

import javax.persistence.*;

@Data
@Entity
@Table(name = "categories")
public class Category extends BaseExcelService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
}
