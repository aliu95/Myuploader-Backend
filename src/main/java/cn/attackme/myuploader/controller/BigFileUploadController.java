package cn.attackme.myuploader.controller;

import cn.attackme.myuploader.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 大文件上传
 */
@RestController
@RequestMapping("/BigFile")
@CrossOrigin
public class BigFileUploadController {
    @Autowired
    private FileService fileService;

    @PostMapping("/")
    public void upload(String name,
                       String md5,
                       Long size,
                       Integer chunks,
                       Integer chunk,
                       MultipartFile file) throws IOException {
        if (chunks != null && chunks != 0) {
            fileService.uploadWithBlock(name, md5,size,chunks,chunk,file);
        } else {
            fileService.upload(name, md5,file);
        }
    }

    @GetMapping("/")
    public Boolean upload2(String name,
                       String md5,
                       Long size,
                       Integer chunks,
                       Integer chunk,
                       MultipartFile file) throws IOException {
        System.out.println(1);
        return false;
    }
}
