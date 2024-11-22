package com.yzu.clp.DATA_PERFORM.controller;


import com.yzu.clp.DATA_PERFORM.service.FileExportService;
import com.yzu.clp.DATA_PERFORM.service.HeiMaoSubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
@Api(tags = "文件下载")
public class FileController {
    @Value("${file.save-path}")
    private String filePath;
    @Value("${file.result.save-path}")
    private String resFilePath;

    @Resource
    private FileExportService fileExportService;
    /**
     * 下载文件
     * @param filename
     * @return
     * @throws MalformedURLException
     */
    @GetMapping("/bulletin/{filename}")
    @ApiOperation("下载文件")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable String filename) throws MalformedURLException {
        Path path = Paths.get(filePath).resolve(filename).normalize();
        org.springframework.core.io.Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    /**
     * 下载算法结果文件
     * @param filename taskid
     * @return
     * @throws MalformedURLException
     */
    @GetMapping("/temp-result-file/{filename}")
    @ApiOperation("下载算法结果文件")
    public ResponseEntity<org.springframework.core.io.Resource> downloadResFileByTaskId(@PathVariable String filename) throws MalformedURLException {
        Path path = Paths.get(resFilePath).resolve(filename).normalize();
        org.springframework.core.io.Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @GetMapping("/generate-bulletin")
    @ApiOperation("下载pdf文件")
    public void downloadBulletin() throws IOException, FontFormatException {
        fileExportService.createBulletinPdf();
    }


}
