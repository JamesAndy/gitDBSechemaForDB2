package com.jc.file;

import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.sql.Timestamp;

/**
 *  File Description: 檔案處理
 *  @author   JamesChang
 *  @since   2020-03-20111:41 OpenJDK 11
 */
public class InputFile {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(InputFile.class.getName());

    /**
     * 建立檔案
     * @param path 目錄
     * @throws IOException
     */
    public void creatFile(String path) throws IOException {
        LOGGER.debug("input path is "+path);
        String checkPath = path.substring(0,path.lastIndexOf("/"));

        File newPath = new File( checkPath );
        //判斷目錄是否存在，不存在則建立
        LOGGER.debug("checkPath is "+checkPath+" is "+newPath.exists());
        if(!newPath.exists()){
            LOGGER.debug("creat dir");
            newPath.mkdirs();
        }
        //判斷檔案是否存在，不存在則建立
        File newFile = new File( path );
        if(!newFile.exists()){
            LOGGER.debug("creat file");
            newFile.createNewFile();
        }
    }

    /**
     * 寫入檔案內容
     * @param goalFileDir 目標文件
     * @param body 檔案內容
     * @param encode 文件編碼
     * @throws IOException
     */
    public void fileWriter(
            String goalFileDir,
            String name,
            Reader body,
            Timestamp creatTimeStamp,
            String encode
    ) throws IOException{
        LOGGER.debug("goalFileDir is "+goalFileDir);
        LOGGER.debug("encode is "+encode);

        PrintWriter pw = new PrintWriter( new OutputStreamWriter(new FileOutputStream(goalFileDir), encode));
        pw.write("/**\r\n");
        pw.write("Name:");pw.write(name);pw.write("\r\n");
        pw.write("CreatTime:");pw.write(creatTimeStamp.toString());pw.write("\r\n");
        pw.write("Power by gitDBSemaForDB2\r\n");
        pw.write("*/\r\n\r\n");
        int intToChar = -1;//將返回內容依次讀取
        while(-1 != (intToChar = body.read())){
            pw.write((char)intToChar);
        }

        pw.flush();
        pw.close();
    }
}
