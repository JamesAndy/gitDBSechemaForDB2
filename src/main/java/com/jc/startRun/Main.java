package com.jc.startRun;

import com.jc.db.LoadDBContent;
import org.apache.logging.log4j.LogManager;

/**
 * 流程控制
 */
public class Main {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {

        var load = new LoadDBContent();

        LOGGER.info("初始化設定...");
        load.setConnents();
        LOGGER.info("開始讀取資料庫，並寫入檔案");
        LOGGER.info("正在取得 Functions...");
        load.getFunctions();
        LOGGER.info("正在取得 Procedures...");
        load.getProcudres();
        LOGGER.info("資料讀取結束");


    }

}
