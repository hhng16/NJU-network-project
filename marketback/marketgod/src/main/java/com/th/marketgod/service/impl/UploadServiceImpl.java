package com.th.marketgod.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.th.marketgod.entity.Upload;
import com.th.marketgod.mapper.UploadMapper;
import com.th.marketgod.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public  class UploadServiceImpl  implements UploadService {

    @Override
    public String uploadFile (MultipartFile file) throws IOException {
        String tackpath = "D:/upload/upimage/";

        File directory = new File(tackpath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        file.transferTo(new File(tackpath+file.getOriginalFilename()));
        return "/images/" + file.getOriginalFilename();
    }
    @Override
    public String uploadHead (MultipartFile file) throws IOException {
        String tackpath = "D:/upload/uphead/";

        File directory = new File(tackpath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        file.transferTo(new File(tackpath+file.getOriginalFilename()));
        return "/heads/" + file.getOriginalFilename();
    }
}
