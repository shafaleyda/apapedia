//package com.apapedia.catalogue.service;
//
//import com.apapedia.catalogue.model.FileData;
//import com.apapedia.catalogue.model.Image;
//import com.apapedia.catalogue.repository.StorageRepository;
//import com.apapedia.catalogue.utils.ImageUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Optional;
//
//public class StorageService {
//
//    @Autowired
//    private StorageRepository repository;
//
//    public String uploadImage(MultipartFile file) throws IOException {
//        Image imageData = repository.save(Image.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .imageData(new byte[][]{ImageUtils.compressImage(file.getBytes())}).build());
//        if (imageData != null) {
//            return "file uploaded successfully";
//        }
//        return null;
//    }
//
//    public byte[] downloadImage(String fileName) {
//        Optional<Image> dbImageData = repository.findByName(fileName);
//        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
//        return images;
//    }
//
//
////    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
////        String filePath=FOLDER_PATH+file.getOriginalFilename();
////
////        FileData fileData=fileDataRepository.save(FileData.builder()
////                .name(file.getOriginalFilename())
////                .type(file.getContentType())
////                .filePath(filePath).build());
////
////        file.transferTo(new File(filePath));
////
////        if (fileData != null) {
////            return "file uploaded successfully : " + filePath;
////        }
////        return null;
////    }
////
////    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
////        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
////        String filePath=fileData.get().getFilePath();
////        byte[] images = Files.readAllBytes(new File(filePath).toPath());
////        return images;
////    }
//
//}
