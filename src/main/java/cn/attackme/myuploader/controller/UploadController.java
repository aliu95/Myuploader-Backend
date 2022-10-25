package cn.attackme.myuploader.controller;

import cn.attackme.myuploader.model.FileInfo;
import cn.attackme.myuploader.model.MultipartFileParams;
import cn.attackme.myuploader.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author luoliang
 * @date 2018/6/19
 */
@RestController
@RequestMapping(value = "/ffmpeg-video",produces = "application/json; charset=UTF-8")
@Slf4j
public class UploadController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 上传前调用(只调一次)，判断文件是否已经被上传完成，如果是，跳过，
     * 如果不是，判断是否传了一半，如果是，将缺失的分片编号返回，让前端传输缺失的分片即可
     * @param file 文件参数
     * @return
     */
    @GetMapping("/upload")
    public ResponseEntity<Map<String,Object>> uploadCheck(MultipartFileParams file){
        log.info("file: "+file);//打印日志
        return fileUploadService.uploadCheck(file);
    }
    /**
     * 上传调用
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<String> upload(MultipartFileParams file){
        log.info("file: "+file);//打印日志
        return fileUploadService.upload(file);
    }

    /**
     * 上传完成调用，进行分片文件合并
     */
    @PostMapping("/upload-success")
    public ResponseEntity<String> uploadSuccess(@RequestBody FileInfo file){
        return fileUploadService.uploadSuccess(file);
    }
}
