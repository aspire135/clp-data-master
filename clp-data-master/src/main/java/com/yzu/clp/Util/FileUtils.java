package com.yzu.clp.Util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static String uploadFile(MultipartFile file, String path, String fileName) {
        String realPath = path + File.separator + fileName;
        File dest = new File(realPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String uploadFile(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        return uploadFile(file, path, fileName);
    }

    public static String uploadFileWithRandomName(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String randomName = System.currentTimeMillis() + suffixName;
        return uploadFile(file, path, randomName);
    }

}
