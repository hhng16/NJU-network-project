package com.th.marketgod.service;

import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UploadService {
    public String uploadFile (MultipartFile file) throws IOException;
    public String uploadHead(MultipartFile file) throws IOException;

}
