package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.GeneratorCodeTicket;

import java.util.Optional;

@Repository
public interface GeneratorCodeTicketRepository extends JpaRepository<GeneratorCodeTicket, Long>, JpaSpecificationExecutor<GeneratorCodeTicket> {
    Optional<GeneratorCodeTicket> findByCodeAndAndCurrentMonthAndGarageId(String code, String currentMonth, Long garageId);
}
