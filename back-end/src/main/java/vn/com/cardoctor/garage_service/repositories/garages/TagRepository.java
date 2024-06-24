package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.garages.Tag;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long>, TagRepositoryCustom {
    List<Tag> findAll();

    @Query(value = "select * from tags where id in :ids", nativeQuery = true)
    List<Tag> findByListId(List<Long> ids);
}
