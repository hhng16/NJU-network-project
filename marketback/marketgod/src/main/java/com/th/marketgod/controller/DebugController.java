package com.th.marketgod.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DebugController {

    @GetMapping("/debug/images")
    public List<String> listImages() {
        File directory = new File("/var/upload/upimage/");
        List<String> files = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        files.add(file.getName());
                    }
                }
            }
        }

        return files;
    }
}

