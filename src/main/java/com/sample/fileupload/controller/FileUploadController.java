package com.sample.fileupload.controller;

import static org.springframework.http.ResponseEntity.status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {

    @Value("${file.upload.directory}")
    String FILE_DIRECTORY;

    @PostMapping("/upload")
    public ResponseEntity<Map> uploadFile(@RequestParam("File") MultipartFile file)
        throws IOException {
        File myFile = new File(FILE_DIRECTORY + file.getOriginalFilename());
        myFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        Map map = new HashMap();
        map.put("message", "success");
        return status(HttpStatus.OK).body(map);
    }

    @DeleteMapping(value = "/delete/{fileName}")
    public ResponseEntity<Map> deleteFile(@PathVariable("fileName") String fileName)
        throws IOException {
        Path path = Paths.get(FILE_DIRECTORY + fileName);
        var isDeleted = Files.deleteIfExists(path);
        Map map = new HashMap();
        map.put("File", fileName);
        if (isDeleted) {
            map.put("response", "success");
            return status(HttpStatus.OK).body(map);
        } else {
            map.put("response", "error");
            return status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}
