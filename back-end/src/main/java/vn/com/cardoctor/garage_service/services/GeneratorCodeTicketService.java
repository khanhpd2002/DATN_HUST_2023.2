package vn.com.cardoctor.garage_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.cardoctor.garage_service.entities.GeneratorCodeTicket;
import vn.com.cardoctor.garage_service.repositories.GeneratorCodeTicketRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class GeneratorCodeTicketService {
    @Autowired
    GeneratorCodeTicketRepository generatorCodeTicketRepository;

    public String generateTicketCode(Long garageId, String code) {
        GeneratorCodeTicket generatorCodeTicket;
        String result;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
        String currentMonthYYMM = currentDate.format(formatter);
        Optional<GeneratorCodeTicket> oGeneratorCodeTicket = generatorCodeTicketRepository.findByCodeAndAndCurrentMonthAndGarageId(code, currentMonthYYMM, garageId);
        if (oGeneratorCodeTicket.isEmpty()) {
            generatorCodeTicket = new GeneratorCodeTicket();
            generatorCodeTicket.setCode(code);
            generatorCodeTicket.setGarageId(garageId);
            generatorCodeTicket.setCurrentTicket(1);
            generatorCodeTicket.setCurrentMonth(currentMonthYYMM);
            this.generatorCodeTicketRepository.save(generatorCodeTicket);
            result = currentMonthYYMM + code + "001";
            return result;
        } else {
            generatorCodeTicket = oGeneratorCodeTicket.get();
            result = currentMonthYYMM + code + String.format("%03d", (generatorCodeTicket.getCurrentTicket() + 1));
            generatorCodeTicket.setCurrentTicket(generatorCodeTicket.getCurrentTicket() + 1);
            generatorCodeTicketRepository.save(generatorCodeTicket);
            return result;
        }
    }

    public String generateCodeInOutBound(Long garageId, String ticketCode, String inOutBoundCode, Integer reason) {
        GeneratorCodeTicket generatorCodeTicket;
        String result;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
        String currentMonthYYMM = currentDate.format(formatter);
        Optional<GeneratorCodeTicket> oGeneratorCodeTicket = generatorCodeTicketRepository.findByCodeAndAndCurrentMonthAndGarageId(ticketCode, currentMonthYYMM, garageId);
        if (oGeneratorCodeTicket.isPresent()) {
            generatorCodeTicket = oGeneratorCodeTicket.get();
            result = currentMonthYYMM + inOutBoundCode + reason + String.format("%03d", (generatorCodeTicket.getCurrentTicket()));
            return result;
        }
        return currentMonthYYMM + inOutBoundCode + reason + "001";
    }
}
