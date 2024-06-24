package vn.com.cardoctor.garage_service.models.responses.tag;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.Tag;

import java.util.List;

@Data
public class TagResponse {
    List<Tag> data;
}
