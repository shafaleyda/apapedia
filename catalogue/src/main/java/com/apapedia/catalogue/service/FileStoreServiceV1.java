//****------------------------NOT USED FOR NOW------------------------****??


//package com.apapedia.catalogue.service;
//
//import com.apapedia.catalogue.config.FileStorageProperties;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.UUID;
//import java.util.stream.Stream;
//
//@Service
//public class FileStoreServiceV1 {
//
//    private FileStorageProperties properties = new FileStorageProperties();
//    Path rootLocation = Paths.get(properties.getUploadDir());
//
//
//
//    public String store(MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                throw new RuntimeException("Failed to store empty file.");
//            }
//
////            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            String extension = StringUtils.cleanPath(file.getOriginalFilename());
//
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