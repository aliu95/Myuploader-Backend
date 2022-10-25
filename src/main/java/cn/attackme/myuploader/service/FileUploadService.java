package cn.attackme.myuploader.service;

import cn.attackme.myuploader.model.FileInfo;
import cn.attackme.myuploader.model.MultipartFileParams;
import cn.attackme.myuploader.utils.MergeFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class FileUploadService {

    //@Value("${upload.file.path}")
    private String uploadFilePath = "D:/develop/video/";

    /**
     * 判断文件是否已经被上传完成，如果是，跳过，
     * 如果不是，判断是否传了一半，如果是，将缺失的分片编号返回，让前端传输缺失的分片即可
     * @param fileParams
     * @return
     */
    public ResponseEntity<Map<String,Object>> uploadCheck(MultipartFileParams fileParams) {
        //获取文件唯一id
        String fileDir = fileParams.getIdentifier();
        String filename = fileParams.getFilename();
        //分片目录
        String chunkPath = uploadFilePath + fileDir+"/chunk/";
        //分片目录对象
        File file = new File(chunkPath);
        //获取分片集合
        List<File> chunkFileList = MergeFileUtil.chunkFileList(file);

        //合并后文件路径
        //合并文件路径，D:/develop/video/文件唯一id/merge/filename
        String filePath = uploadFilePath + fileDir+"/merge/"+filename;
        File fileMergeExist = new File(filePath);

        String [] temp;//保存已存在文件列表
        boolean isExists = fileMergeExist.exists();//是否已经纯在合并完成的文件
        if(chunkFileList == null){
            temp= new String[0];
        }else{
            temp = new String[chunkFileList.size()];
            //如果没有合并后文件，代表没有上传完成
            //没上传完，如果有切片，保存已存在切片列表，否则不保存
            if(!isExists && chunkFileList.size()>0){
                for(int i = 0;i<chunkFileList.size();i++){
                    temp[i] = chunkFileList.get(i).getName();//保存已存在文件列表
                }
            }
        }

        //返回结果集
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code",1);
        hashMap.put("message","Success");
        hashMap.put("needSkiped",isExists);
        hashMap.put("uploadList",temp);

        return ResponseEntity.ok(hashMap);
    }

    /**D:/develop/video/文件唯一id/chunk/分片编号
     * 分片上传文件
     * @param fileParams 文件参数
     * @return
     */
    public ResponseEntity<String> upload(MultipartFileParams fileParams) {

        //获取文件唯一id
        String fileDir = fileParams.getIdentifier();
        //分片编号
        int chunkNumber = fileParams.getChunkNumber();
        //文件路径，文件具体路径，D:/develop/video/文件唯一id/chunk/1
        String filePath = uploadFilePath + fileDir+"/chunk/"+chunkNumber;

        File fileTemp = new File(filePath);

        File parentFile = fileTemp.getParentFile();

        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            MultipartFile file = fileParams.getFile();
            //使用file.transferTo(dest)方法将上传文件file写到服务器上指定的dest文件;
            //只能使用一次，原因是文件流只可以接收读取一次，传输完毕则关闭流;
            file.transferTo(fileTemp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("SUCCESS");
    }

    public ResponseEntity<String> uploadSuccess(FileInfo fileInfo) {
        log.info("filename: "+fileInfo.getName());
        log.info("UniqueIdentifier: "+fileInfo.getUniqueIdentifier());
        //分片目录路径
        String chunkPath = uploadFilePath + fileInfo.getUniqueIdentifier()+"/chunk/";
        //合并目录路径
        String mergePath = uploadFilePath + fileInfo.getUniqueIdentifier()+"/merge/";
        //合并文件，D:/develop/video/文件唯一id/merge/filename
        File file = MergeFileUtil.mergeFile(uploadFilePath,chunkPath, mergePath,fileInfo.getName());
        if(file == null){
            return ResponseEntity.ok("ERROR:文件合并失败");
        }
        return ResponseEntity.ok("SUCCESS");
    }
}
