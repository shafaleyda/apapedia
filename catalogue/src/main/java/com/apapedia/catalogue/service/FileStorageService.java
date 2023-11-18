
//****------------------------NOT USED FOR NOW------------------------****??


//package com.apapedia.catalogue.service;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//import com.apapedia.catalogue.config.FileStorageProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import com.apapedia.catalogue.*;
//
//@Service
//public class FileStorageService {
//    private final Path fileStorageLocation;
//    @Autowired
//    public FileStorageService(FileStorageProperties fileStorageProperties) {
//        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new RuntimeException("Couldn't create the directory where the upload files will be saved.", ex);
//        }
//    }
//    public String storeFile(MultipartFile file) {
//        // Normalize file name
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        try {
//            // Check if the file's name contains valid  characters or not
//            if (fileName.contains("..")) {
//                throw new RuntimeException("Sorry! File name which contains invalid path sequence " + fileName);
//            }
//            // Copy file to the target place (Replacing existing file with the same name)
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return fileName;
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return fileName;
//    }
//}