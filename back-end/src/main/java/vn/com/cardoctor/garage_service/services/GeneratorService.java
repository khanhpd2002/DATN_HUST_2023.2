package vn.com.cardoctor.garage_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.cardoctor.garage_service.repositories.GeneratorRepository;

@Service
public class GeneratorService {
    @Autowired
    GeneratorRepository generatorRepository;


}
