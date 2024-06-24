package vn.com.cardoctor.garage_service.aop;

import model.BaseResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.services.EmailService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandle.class);

    @Autowired
    EmailService emailService;

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<?>> handleCustomizedException(ApiException e) {
        LOGGER.info("Exception {}", e.getMessage());
        return new ResponseEntity<>(new BaseResponse<>(e.getCode(), e.getMessage(), e.getData()), HttpStatus.OK);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<BaseResponse<?>> handleInternalException(Exception ex) {
        try {
            LOGGER.error("Exception ", ex);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String stackTrace = sw.toString(); // stack trace as a string
            Thread thread = new Thread(() -> emailService.sendLogError(stackTrace.replace(System.getProperty("line.separator"), "<br/>\n")));
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new BaseResponse<>(ERROR.SYSTEM_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        LOGGER.error("Validation Exception ");
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" \n "));
        BaseResponse<?> response = new BaseResponse<>(ERROR.INVALID_REQUEST.getCode(), errors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

