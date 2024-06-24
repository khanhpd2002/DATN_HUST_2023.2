package vn.com.cardoctor.garage_service.repositories;

import vn.com.cardoctor.garage_service.entities.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
}
