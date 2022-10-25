package cn.attackme.myuploader.service;

import cn.attackme.myuploader.config.UploadConfig;
import cn.attackme.myuploader.model.MultipartFileParams;
import cn.attackme.myuploader.utils.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author liuao
 * @date 2022/10/12 14:16
 */
@Service
public class FileService2 {

    //@Value("${upload.file.path}")
    private String uploadFilePath = "D:/develp/video";

    public ResponseEntity<String> upload(MultipartFileParams fileParams) throws IOException {
        String filePath = uploadFilePath + fileParams.getFilename();
        File fileTemp = new File(filePath);

        File parentFile = fileTemp.getParentFile();

        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            MultipartFile file = fileParams.getFile();
            //使用transferTo(dest)方法将上传文件写到服务器上指定的文件;
            //只能使用一次，原因是文件流只可以接收读取一次，传输完毕则关闭流;
            file.transferTo(fileTemp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("SUCCESS");
    }

}
