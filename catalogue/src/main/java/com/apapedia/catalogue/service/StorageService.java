package com.apapedia.catalogue.service;

import com.apapedia.catalogue.model.ImageData;
import com.apapedia.catalogue.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.apapedia.catalogue.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.util.StringUtils;

@Service
public class StorageService {

    private Path fileStorageLocation;

    @Autowired
    private StorageRepository repository;

    @Autowired
    private ImageData imageDb;

    @Autowired
    public void FileStorageService(FileStorage fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create the directory where the upload files will be saved.", ex);
        }
    }

    public StorageService(Path fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            // Check if the file's name contains valid  characters or not
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! File name which contains invalid path sequence " + fileName);
            }
            // Copy file to the target place (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}


//    public String uploadImage(MultipartFile file) throws IOException {
//        ImageData imageData = repository.save(ImageData.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .imageDb(new byte[]{ImageUtils.compressImage(file.getBytes())}).build());
//        if (imageData != null) {
//            return "file uploaded successfully";
//        }
//        return null;
//    }


