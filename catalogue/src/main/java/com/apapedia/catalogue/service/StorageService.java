//****------------------------NOT USED FOR NOW------------------------****??


//package com.apapedia.catalogue.service;
//
////import com.apapedia.catalogue.model.ImageData;
////import com.apapedia.catalogue.repository.StorageRepository;
//import com.apapedia.catalogue.config.StorageProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import com.apapedia.catalogue.utils.*;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.Objects;
//
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.util.UUID;
//import java.util.stream.Stream;
//
//import org.springframework.util.StringUtils;
//
//@Service
//public class StorageService {
//
//    private StorageProperties properties = new StorageProperties();
//    Path rootLocation = Paths.get(properties.getLocation());
//
//    public String store(MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                throw new RuntimeException("Failed to store empty file.");
//            }
//
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            String uploadedFileName = UUID.randomUUID().toString() + "." + extension;
//
//            Path destinationFile = rootLocation.resolve(
//                            Paths.get(uploadedFileName))
//                    .normalize().toAbsolutePath();
//
//            try (InputStream inputStream = file.getInputStream()) {
//                Files.copy(inputStream, destinationFile,
//                        StandardCopyOption.REPLACE_EXISTING);
//
//                final String baseUrl =
//                        ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//
//                return baseUrl+"/fileUpload/files/"+uploadedFileName;
//            }
//        }
//        catch (IOException e) {
//            throw new RuntimeException("Failed to store file.", e);
//        }
//    }
//
//    public Stream<Path> loadAll() {
//        try {
//            return Files.walk(this.rootLocation, 1)
//                    .filter(path -> !path.equals(this.rootLocation))
//                    .map(this.rootLocation::relativize);
//        }
//        catch (IOException e) {
//            throw new RuntimeException("Failed to read stored files", e);
//        }
//
//    }
//
//    public Resource load(String filename) {
//        try {
//            Path file = rootLocation.resolve(filename);
//            Resource resource = new UrlResource(file.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("Could not read the file!");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Error: " + e.getMessage());
//        }
//    }
//}
//
//
////    public String uploadImage(MultipartFile file) throws IOException {
////        ImageData imageData = repository.save(ImageData.builder()
////                .name(file.getOriginalFilename())
////                .type(file.getContentType())
////                .imageDb(new byte[]{ImageUtils.compressImage(file.getBytes())}).build());
////        if (imageData != null) {
////            return "file uploaded successfully";
////        }
////        return null;
////    }
//
//
