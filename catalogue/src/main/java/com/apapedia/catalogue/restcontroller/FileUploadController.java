//package com.apapedia.catalogue.restcontroller;
//
//import com.apapedia.catalogue.model.ImageData;
////import com.apapedia.catalogue.service.FileStorageService;
//import com.apapedia.catalogue.service.FileStoreServiceV1;
////import com.apapedia.catalogue.service.StorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
//
//import java.nio.file.Path;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@RestController
//@RequestMapping("/fileUpload")
//public class FileUploadController {
//
//    @Autowired
//    FileStoreServiceV1 fileStoreService;
//
//    @PostMapping("/")
//    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
//        return fileStoreService.store(file);
//    }
//
//
//    @GetMapping("/")
//    public ResponseEntity<List<ImageData>> getListFiles() {
//        List<ImageData> fileInfos = fileStoreService.loadAll().map(path -> {
//            String filename = path.getFileName().toString();
//            String url = MvcUriComponentsBuilder
//                    .fromMethodName(FileUploadController.class, "getFile", path.getFileName().toString()).build().toString();
//
//            return new ImageData(filename, url);
//        }).collect(Collectors.toList());
//
//        Stream<Path> pathStream = fileStoreService.loadAll();
//        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//    }
//
//    @GetMapping("/files/{filename:.+}")
//    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//        Resource file = fileStoreService.load(filename);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }
//
//}