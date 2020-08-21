package com.jc.db;

import com.jc.file.InputFile;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 取得內容
 */
public class LoadDBContent {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(LoadDBContent.class.getName());
    private static DbConnent dbConnet = new DbConnent();
    private String whereFunctionSQL = "";
    private String whereProceduresSQL = "";
    private String parentPath = "";

    /**
     * 設定連線與新關參數
     */
    public void setConnents(){
        LOGGER.debug("設定連線參數");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try(
                InputStream dbContent = classloader.getResourceAsStream("dbContent.properties");
                InputStream dbSchema = classloader.getResourceAsStream("dbSchema.properties");
                InputStream gitContent = classloader.getResourceAsStream("gitContent.properties");
        ){
            var propDbContent = new Properties();
            var propDbSchema = new Properties();
            var propGitContent= new Properties();

            // load a properties file
            propDbContent.load(dbContent);
            propDbSchema.load(dbSchema);
            propGitContent.load(gitContent);

            dbConnet.setJdbcDriver(propDbContent.getProperty("db.jdbcDriver"));
            dbConnet.setDbUrl(propDbContent.getProperty("db.url"));
            dbConnet.setUser(propDbContent.getProperty("db.user"));
            dbConnet.setPassWord(propDbContent.getProperty("db.passWord"));
            whereFunctionSQL = propDbSchema.getProperty("function.where.sql");
            whereProceduresSQL = propDbSchema.getProperty("procedures.where.sql");
            parentPath = propGitContent.getProperty("git.local.parentPath");
        }
        catch (IOException ex){
            LOGGER.error(ex.getMessage());
            throw new RuntimeException();
        }

    }

    /**
     * 取得function內容
     */
    public void getFunctions(){

        /*宣告元件*/
        var inputFile = new InputFile();
        var localPath = parentPath+"functions/";

        LOGGER.debug("參數設定完畢");

        try(
                var conn = DriverManager.getConnection(dbConnet.getDbUrl(),dbConnet.getUser(), dbConnet.getPassWord());
                var statement = conn.createStatement();
        ){
            LOGGER.debug("資料庫連線成功");

            LOGGER.debug("執行查詢開始");

            var querySQL = new StringBuilder();
            querySQL.append(" SELECT ");
            querySQL.append(" t.NAME ");
            querySQL.append(" ,t.CREATE_TIME ");
            querySQL.append(" ,t.BODY  ");
            querySQL.append(" FROM SYSIBM.SYSFUNCTIONS T ");
            querySQL.append(" WHERE 1=1 ");
            querySQL.append(whereFunctionSQL);
            querySQL.append(" WITH UR ");


            LOGGER.debug(querySQL);
            var result = statement.executeQuery(querySQL.toString());
            LOGGER.debug("執行查詢結束");

            LOGGER.debug("執行匯出資料開始");
            while (result.next()){
                var filePathFull = new StringBuilder();
                filePathFull.append(localPath);
                filePathFull.append(result.getString("NAME"));
                filePathFull.append(".sql");

                LOGGER.debug(result.getTimestamp("CREATE_TIME"));
                LOGGER.debug(result.getClob("BODY"));

                LOGGER.debug("filePathFull is "+filePathFull.toString());
                Reader body = result.getClob("BODY").getCharacterStream();

                inputFile.creatFile(filePathFull.toString());
                inputFile.fileWriter(filePathFull.toString(),result.getString("NAME"),body,result.getTimestamp("CREATE_TIME"),"UTF-8");

            }
            LOGGER.debug("執行匯出資料結束");


        }
        catch (SQLException |IOException ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeException();
        }
    }

    public void getProcudres(){

        var inputFile = new InputFile();
        var localPath = parentPath+"procedures/";

        LOGGER.debug("參數設定完畢");

        try(
                var conn = DriverManager.getConnection(dbConnet.getDbUrl(),dbConnet.getUser(), dbConnet.getPassWord());
                var statement = conn.createStatement();
        ){
            LOGGER.debug("資料庫連線成功");

            LOGGER.debug("執行查詢開始");

            var querySQL = new StringBuilder();
            querySQL.append(" SELECT ");
            querySQL.append(" t.PROCNAME NAME ");
            querySQL.append(" ,t.CREATE_TIME ");
            querySQL.append(" ,t.TEXT BODY  ");
            querySQL.append(" FROM SYSCAT.PROCEDURES T ");
            querySQL.append(" WHERE 1=1 ");
            querySQL.append(whereProceduresSQL);
            querySQL.append(" WITH UR ");


            LOGGER.debug(querySQL);
            var result = statement.executeQuery(querySQL.toString());
            LOGGER.debug("執行查詢結束");

            LOGGER.debug("執行匯出資料開始");
            while (result.next()){
                var filePathFull = new StringBuilder();
                filePathFull.append(localPath);
                filePathFull.append(result.getString("NAME"));
                filePathFull.append(".sql");

                LOGGER.debug(result.getTimestamp("CREATE_TIME"));
                LOGGER.debug(result.getClob("BODY"));

                LOGGER.debug("filePathFull is "+filePathFull.toString());
                Reader body = result.getClob("BODY").getCharacterStream();

                inputFile.creatFile(filePathFull.toString());
                inputFile.fileWriter(filePathFull.toString(),result.getString("NAME"),body,result.getTimestamp("CREATE_TIME"),"UTF-8");

            }
            LOGGER.debug("執行匯出資料結束");


        }
        catch (SQLException |IOException ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeException();
        }
    }

}
