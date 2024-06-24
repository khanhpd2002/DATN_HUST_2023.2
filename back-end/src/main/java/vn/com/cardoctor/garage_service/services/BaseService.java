package vn.com.cardoctor.garage_service.services;

import authentication.UserAuthentication;
import authentication.UserInfoContext;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.Generator;
import vn.com.cardoctor.garage_service.entities.TimeConfig;
import vn.com.cardoctor.garage_service.repositories.GeneratorRepository;
import vn.com.cardoctor.garage_service.repositories.TimeConfigRepository;
import vn.com.cardoctor.garage_service.utils.FormatUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static vn.com.cardoctor.garage_service.aop.LogAspect.USER_INFO;

@Log4j2
public class BaseService {
    @Autowired
    GeneratorRepository generatorRepository;

    @Autowired
    TimeConfigRepository timeConfigRepository;

    @Autowired
    protected HttpServletRequest httpServletRequest;

    protected String getGeneratorValue(String code) {
        Long suffixNumber = null;
        Optional<Generator> oGenerator = this.generatorRepository.findByCode(code);
        if (oGenerator.isPresent()) {
            try {
                suffixNumber = Long.parseLong(oGenerator.get().getValue()) + 1;
            } catch (NumberFormatException e) {
                BaseService.log.error(e.getMessage(), e);
                return null;
            }
        } else {
            suffixNumber = 1L;
        }
        Generator generator = oGenerator.get();
        generator.setValue(suffixNumber.toString());
        this.generatorRepository.save(generator);
        return suffixNumber.toString();
    }

    protected List<String> convertAppointmentDateToBuildNotification(Date appointmentDate, Integer timeFrame) throws ApiException {
        List<String> times = new ArrayList<>();
        Optional<TimeConfig> oTimeConfig = this.timeConfigRepository.findByTimeFrame(timeFrame);
        if (oTimeConfig.isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST);
        }
        TimeConfig timeConfig = oTimeConfig.get();
        int minutesToSubtract = timeConfig.getNotifyBefore();
        Date startTime = timeConfig.getStartTime();
        Date endTime = timeConfig.getEndTime();

        LocalDateTime localDateTime = appointmentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        String localDateTimeString = FormatUtil.formatterSlash.format(Date.from(zonedDateTime.toInstant()));

        Date startDate = new Date(startTime.getTime());
        LocalDateTime startLocalDateTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime updatedStartLocalDateTime = startLocalDateTime.minusMinutes(minutesToSubtract);
        ZonedDateTime zonedStartDateTime = updatedStartLocalDateTime.atZone(ZoneId.systemDefault());
        String updatedStartDate = FormatUtil.timeFormat.format(Date.from(zonedStartDateTime.toInstant()));

        String startDateString = localDateTimeString + " " + updatedStartDate;

        Date endDate = new Date(startTime.getTime());
        LocalDateTime endLocalDateTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime updatedLocalDateTime = endLocalDateTime.minusMinutes(minutesToSubtract);
        ZonedDateTime zonedEndDateTime = updatedLocalDateTime.atZone(ZoneId.systemDefault());
        String updatedEndDate = FormatUtil.timeFormat.format(Date.from(zonedEndDateTime.toInstant()));
        String endDateString = localDateTimeString + " " + updatedEndDate;

        times.add(startDateString);
        times.add(endDateString);
        return times;
    }

    protected String getKeyCloakUserId() {
        String userInfo = httpServletRequest.getHeader(USER_INFO);
        log.info("user info {}", userInfo);
        if (Strings.isNotEmpty(userInfo)) {
            UserInfoContext.add(userInfo);
            UserAuthentication userAuthentication = UserInfoContext.getUserInfo();
            log.info("============= Keycloak id is {} ===========", userAuthentication.getUserId());
            return userAuthentication.getUserId();
        }
        return null;
    }

    protected String getKeyCloakUsername() {
        String userInfo = httpServletRequest.getHeader(USER_INFO);
        log.info("user info {}", userInfo);
        if (Strings.isNotEmpty(userInfo)) {
            UserInfoContext.add(userInfo);
            UserAuthentication userAuthentication = UserInfoContext.getUserInfo();
            log.info("============= Username is {} ===========", userAuthentication.getUserName());
            return userAuthentication.getUserName();
        }
        return null;
    }
}
