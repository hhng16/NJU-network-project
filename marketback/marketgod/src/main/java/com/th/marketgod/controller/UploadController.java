package com.th.marketgod.controller;

import com.th.marketgod.service.UploadService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private UploadService uploadService;

    @PostMapping("/uploadit")
    public String uploadFile(MultipartFile file) throws IOException {
        return uploadService.uploadFile(file);
    }
    @PostMapping("/uploadhead")
    public String uploadHead(MultipartFile file) throws IOException {
        return uploadService.uploadHead(file);
    }
}