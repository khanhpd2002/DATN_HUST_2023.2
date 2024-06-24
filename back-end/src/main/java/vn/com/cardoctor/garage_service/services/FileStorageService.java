package vn.com.cardoctor.garage_service.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.configs.FileStorageProperties;
import vn.com.cardoctor.garage_service.models.dtos.FileResponseDto;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService extends BaseService {
    private static final Logger log = LogManager.getLogger(FileStorageService.class);
    private final Path fileStorageLocation;
    private final String tempExportExcel;
    private final String libreOfficePath;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws ApiException {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.tempExportExcel = fileStorageProperties.getTempExportExcel();
        this.libreOfficePath = fileStorageProperties.getLibreOfficePath();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new ApiException(ERROR.INVALID_REQUEST);
        }
    }

    public String getTempExportExcel() {
        return this.tempExportExcel;
    }

    public String getLibreOfficePath() {
        return this.libreOfficePath;
    }

    public Resource loadFileAsResource(String fileName) throws ApiException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "File not found " + fileName + "\n" + ex.getMessage());
        }
    }

    public HttpHeaders loadHttpHeaders(Resource resource) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, new MimetypesFileTypeMap().getContentType(resource.getFile()));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        return headers;
    }

    public String saveFile(String relativePath, MultipartFile multipartFile) throws ApiException {
        Path folderPath = Paths.get(this.fileStorageLocation.normalize().toString(), relativePath).normalize();
        String fileName = multipartFile.getOriginalFilename();
        Path filePath = folderPath.resolve(fileName);
        if (!folderPath.toFile().exists()) {
            try {
                FileUtils.forceMkdirParent(filePath.toFile());
            } catch (IOException e) {
                FileStorageService.log.error(e.getMessage(), e);
                throw new ApiException(ERROR.INVALID_REQUEST, "Không thể lưu file");
            }
        }
        while (filePath.toFile().exists()) {
            String prefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            filePath = folderPath.resolve(prefix + "_" + fileName);
        }
        try {
            multipartFile.transferTo(filePath);
        } catch (IllegalStateException | IOException e) {
            FileStorageService.log.error(e.getMessage(), e);
            throw new ApiException(ERROR.INVALID_REQUEST, "Không thể lưu file");
        }
        return this.fileStorageLocation.normalize().relativize(filePath).normalize().toString().replace("\\", "/");
    }

    public FileResponseDto uploadFile(String filePath, MultipartFile file) throws ApiException {
        LocalDateTime sysdate = LocalDateTime.now();
        String fileUrl = this.saveFile(String.format("%04d/%02d/%s", sysdate.getYear(), sysdate.getMonthValue(), filePath), file);
        return FileResponseDto.builder().url(fileUrl).build();
    }

    public List<FileResponseDto> uploadFileV2(String filePath, List<MultipartFile> files) throws ApiException {
        log.info("==============> Files length: {}",files.size());
        List<FileResponseDto> fileResponseDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            LocalDateTime sysdate = LocalDateTime.now();
            String fileUrl = this.saveFile(String.format("%04d/%02d/%s", sysdate.getYear(), sysdate.getMonthValue(), filePath), file);
            fileResponseDtos.add(FileResponseDto.builder().url(fileUrl).build());
        }
        return fileResponseDtos;
    }

    public Boolean deleteFile(List<String> relativeFilePaths) {
        List<File> files = new ArrayList<>();
        for (String relativeFilePath : relativeFilePaths) {
            Path filePath = Paths.get(this.fileStorageLocation.normalize().toString(), relativeFilePath).normalize();
            if (!filePath.toFile().exists()) {
                return false;
            }
            files.add(filePath.toFile());
        }
        for (File file : files) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                FileStorageService.log.error(e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    public FileResponseDto deleteFileV2(List<String> relativeFilePaths) {
        Boolean result = this.deleteFile(relativeFilePaths);
        return FileResponseDto.builder().deleted(result).build();
    }

    public String getAbsolutePath(String relativePath) {
        return Paths.get(this.fileStorageLocation.normalize().toString(), relativePath).normalize().toFile()
                .getAbsolutePath();
    }

    public List<String> getFilesExist(List<String> fileNames) {
        List<String> urlsExist = new ArrayList<>();
        for (String fileName : fileNames) {
            try {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists()) {
                    urlsExist.add(fileName);
                }
            } catch (MalformedURLException ignored) {
            }
        }
        return urlsExist;
    }
}



