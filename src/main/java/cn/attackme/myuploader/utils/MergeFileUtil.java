package cn.attackme.myuploader.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class MergeFileUtil {
    /**
     * 判断要上传分片的目录路径存不存在，不存在就创建，
     * @param filePath 分片路径
     * @return File对象
     */
    public static File isUploadChunkParentPath(String filePath){

        File fileTemp = new File(filePath);

        File parentFile = fileTemp.getParentFile();

        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        return fileTemp;
    }

    /**
     * 合并文件，D:/develop/video/文件唯一id/merge/filename
     * @param uploadPath 上传路径 D:/develop/video/
     * @param chunkPath 分片文件目录路径
     * @param mergePath 合并文件目录D:/develop/video/文件唯一id/merge/
     * @param fileName 文件名
     * @return
     */
    public static File mergeFile(String uploadPath,String chunkPath,String mergePath,String fileName){
        //得到块文件所在目录
        File file = new File(chunkPath);

        List<File> chunkFileList = chunkFileList(file);

        //合并文件前，先判断是否有合并目录，没有创建
        File fileTemp = new File(mergePath);

        if(!fileTemp.exists()){
            fileTemp.mkdirs();
        }

        //合并文件路径
        File mergeFile = new File(mergePath + fileName);
        // 合并文件存在先删除再创建
        if(mergeFile.exists()){
            mergeFile.delete();
        }
        boolean newFile = false;
        try {
            newFile = mergeFile.createNewFile();//创建文件，已存在返回false，不存在创建文件，目录不存在直接抛异常
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!newFile){//如果newFile=false，表示文件存在
            return null;
        }
        try {
            //创建写文件对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile,"rw");
            //遍历分块文件开始合并
            // 读取文件缓冲区
            byte[] b = new byte[1024];
            for(File chunkFile:chunkFileList){
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"r");
                int len =-1;
                //读取分块文件
                while((len = raf_read.read(b))!=-1){
                    //向合并文件中写数据
                    raf_write.write(b,0,len);
                }
                raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mergeFile;
    }

    /**
     * 获取指定块文件目录所有文件
     * @param file 块目录
     * @return 文件list
     */
    public static List<File> chunkFileList(File file){
        //获取目录所有文件
        File[] files = file.listFiles();
        if(files == null){
            return null;
        }
        //转换为list，方便排序
        List<File> chunkFileList = new ArrayList<>();
        chunkFileList.addAll(Arrays.asList(files));
        //排序
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;
            }
        });
        return chunkFileList;
    }
}
