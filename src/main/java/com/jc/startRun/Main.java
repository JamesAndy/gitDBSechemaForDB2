package com.jc.startRun;

import com.jc.db.LoadDBContent;
import com.jc.turn.JcFile;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

import static java.lang.System.out;

/**
 * 流程控制
 */
public class Main  {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Main.class.getName());

    //參數設定:設定轉碼目標
    private final static String trunEncode = "UTF-8";

    //參數設定:起始目錄
    private final static String startPath = "E:/Desktop/db2/";
    //參數設定:篩選檔案
    private final static String pFilter = ".sql";
    //參數設定:取代路徑
    private final static String replacePath = "E:/Desktop/finish/";
    //參數設定:目的路徑
    private final static String purposePath = "E:/Desktop/finish/";

    public static void main(String[] args) throws Exception {

//        var load = new LoadDBContent();

//        LOGGER.info("初始化設定...");
//        load.setConnents();
//        LOGGER.info("開始讀取資料庫，並寫入檔案");
//        LOGGER.info("正在取得 Functions...");
//        load.getFunctions();
//        LOGGER.info("正在取得 Procedures...");
//        load.getProcudres();
//        LOGGER.info("資料讀取結束");

        //取得要轉檔的檔案路徑
        JcFile.getPathDir(startPath,pFilter);

        LOGGER.info("startPath = "+startPath);
        LOGGER.info("pFilter = "+pFilter);

        //執行
        for( String fileSourceDir : JcFile.gFileList ){

            LOGGER.info("replacePath = "+replacePath);
            LOGGER.info("purposePath = "+purposePath);


            String goalFileDir = fileSourceDir.replace(startPath,purposePath);

            String code = JcFile.checkEncoding(fileSourceDir);

            if(code != null){
                LOGGER.info("goalFileDir = "+goalFileDir);
                JcFile.creatFile(goalFileDir);

                //寫入檔案內容
                JcFile.convertFileCode(fileSourceDir,goalFileDir,code,trunEncode);
            }

        }


    }

}
