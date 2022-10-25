package cn.attackme.myuploader.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartFileParams {
    //分片编号
    private int chunkNumber;
    //分片大小
    private long chunkSize;
    //当前分片大小
    private long currentChunkSize;
    //文件总大小
    private long totalSize;
    //分片id
    private String identifier;
    //文件名
    private String filename;
    //相对路径
    private String relativePath;
    //总分片数
    private int totalChunks;
    //spring 接收前端传输来的文件对象
    private MultipartFile file;
}
