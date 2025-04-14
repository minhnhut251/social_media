package com.da2.socialmedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final String uploadDir = "./uploads/images";

    @Autowired
    public FileService(){
        // Create the uploads directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

//    public String handleFileUpload(MultipartFile file) {
//        try {
//            // Generate a unique filename
//            String originalFilename = file.getOriginalFilename();
//            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            String newFilename = UUID.randomUUID().toString() + fileExtension;
//
//            // Save the file to the upload directory
//            Path filePath = Paths.get(uploadDir, newFilename);
//            Files.write(filePath, file.getBytes());
//
//            // Return the relative path
//            return "/uploads/images/" + newFilename;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to upload file", e);
//        }
//    }
public String handleFileUpload(MultipartFile file) {
    try {
        // Generate a unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        // Save the file to the upload directory
        Path filePath = Paths.get(uploadDir, newFilename);
        Files.write(filePath, file.getBytes());

        // Return the relative path (keeping original structure)
        return "/uploads/images/" + newFilename;
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to upload file", e);
    }
}

    public void deleteFileIfExists(String mediaURL) {
        if (mediaURL != null && !mediaURL.isEmpty()) {
            // Extract the filename from the path
            String fileName = mediaURL.substring(mediaURL.lastIndexOf("/") + 1);
            File file = new File(uploadDir, fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
