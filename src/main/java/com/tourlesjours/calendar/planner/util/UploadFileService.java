package com.tourlesjours.calendar.planner.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Service
public class UploadFileService {

    public String upload(String id, MultipartFile file) {

        boolean result = false;

        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        String uploadDir = "c:\\calendar\\upload\\" + id;

        UUID uuid = UUID.randomUUID();
        String uniqueFileName = uuid.toString().replaceAll("-", "");

        File saveFile = new File(uploadDir + "\\" + uniqueFileName + fileExtension);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        try {
            file.transferTo(saveFile);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result) {
            return uniqueFileName + fileExtension;
        } else {
            return null;
        }
    }
}
