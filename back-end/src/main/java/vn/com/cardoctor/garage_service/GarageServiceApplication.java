package vn.com.cardoctor.garage_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import utils.ThreadLocalUtils;
import vn.com.cardoctor.garage_service.services.FileStorageService;
import vn.com.cardoctor.garage_service.services.KeycloakService;
import vn.com.cardoctor.garage_service.services.excels.DownloadExcelService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@SpringBootApplication(scanBasePackages= {"vn.com.cardoctor.garage_service"})
@Log4j2
@EnableTransactionManagement
@EnableAsync
@EnableFeignClients
@RequiredArgsConstructor
public class GarageServiceApplication implements CommandLineRunner {
    private final DownloadExcelService downloadExcelService;
    private final FileStorageService fileStorageService;

    public static void main(String[] args) {
        SpringApplication.run(GarageServiceApplication.class, args);


        System.out.println("==================================================");
        System.out.println("|             AC GARAGE SERVICE                      |");
        System.out.println("|           DEPLOY BY DEVELOP TEAM                   |");
        System.out.println("==================================================");

        log.info("==================================================");
        log.info("|                AC GARAGE SERVICE               |");
        log.info("|             DEPLOY BY DEVELOP TEAM             |");
        log.info("==================================================");
        try {
            log.info("================================================== {}", new ObjectMapper().writeValueAsString(Collections.emptyList()));
        } catch (Exception e) {
            log.info(e);
        }


    }

    @Bean
    public ThreadLocalUtils newThreadLocal() {
        return new ThreadLocalUtils();
    }

    @Override
    public void run(String... args) throws Exception {
        this.downloadExcelService.downloadTemplateCarCustomer();
        // Đường dẫn đến file A
        String sourceFilePath = this.fileStorageService.getTempExportExcel() + "/Template_import_CarCustomer.xlsx";
        // Đường dẫn đích
        String destinationDirectoryPath = "templates/import-data/";

        // Thử di chuyển file
        try {
            moveFile(sourceFilePath, destinationDirectoryPath);
            log.error("File đã được di chuyển thành công.");
        } catch (IOException e) {
            log.error("Đã xảy ra lỗi khi di chuyển file: {}", e.getMessage());
        }
    }


    private static void moveFile(String sourceFilePath, String destinationDirectoryPath) throws IOException {
        // Tạo đối tượng File cho file nguồn
        File sourceFile = new File(sourceFilePath);

        // Kiểm tra xem file nguồn có tồn tại không
        if (!sourceFile.exists()) {
            throw new IOException("File nguồn không tồn tại.");
        }

        // Tạo đối tượng File cho thư mục đích
        File destinationDirectory = new File(destinationDirectoryPath);

        // Kiểm tra xem thư mục đích có tồn tại không
        if (!destinationDirectory.exists()) {
            // Nếu không tồn tại, tạo thư mục đích
            if (!destinationDirectory.mkdirs()) {
                throw new IOException("Không thể tạo thư mục đích.");
            }
        }

        // Tạo đối tượng Path cho file đích
        Path destinationPath = Paths.get(destinationDirectoryPath, sourceFile.getName());

        // Di chuyển file
        Files.move(sourceFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
