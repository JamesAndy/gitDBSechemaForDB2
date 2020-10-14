package com.jc.turn;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *  File Description: 檔案處理
 *  @author   JamesChang
 *  @since   2020-03-20111:41 OpenJDK 14
 */
public class JcFile {


    /** 檔案集合 */
    public static List<String> gFileList = new ArrayList<String>();

    /**
     * 建立檔案
     * @param path
     * @throws IOException
     */
    public static void creatFile(String path) throws IOException {
        String checkPath = path.substring(0,path.lastIndexOf("/"));
        java.io.File newPath = new java.io.File( checkPath );
        //判斷目錄是否存在，不存在則建立
        if(!newPath.exists()){
            newPath.mkdirs();
        }
        //判斷檔案是否存在，不存在則建立
        java.io.File newFile = new java.io.File( path );
        if(!newFile.exists()){
            newFile.createNewFile();
        }
    }

    /**
     * 取得要處理的檔案
     * @param startPath
     * @param fileFilter
     */
    public static void getPathDir(String startPath,String fileFilter){
        File topPathFile = new File( startPath );
        String[] filePathArray = topPathFile.list();
        if(filePathArray != null){
            for(int i=0 ; i< filePathArray.length ; i++){
                String filePath = startPath+"/"+ filePathArray[i];
                File file = new File( filePath);
                //判斷是否為檔案
                if(file.isFile()){
                    if( fileFilter.equals("*") || file.getPath().indexOf(fileFilter) >= 0 )
                        gFileList.add( startPath + "/" + filePathArray[i] );
                }
                else {
                    getPathDir(filePath,fileFilter);
                }
            }
        }
        else {
            gFileList.add( startPath );
        }

    }


    /**
     * 確認檔案編碼
     * @param filePath 檔案路徑
     * @return [String]charset:編碼格式
     * @throws IOException
     */
    public static String checkEncoding(String filePath) throws IOException {

        /* 讀取檔案 */
        FileInputStream fis = new FileInputStream(filePath);
        /* 建立分析器 */
        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        byte[] buf = new byte[4];
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            /* 分析資料 */
            detector.handleData(buf, 0, nread);
        }
        fis.close();
        detector.dataEnd();
        /* 取得編碼格式 */
        String charset = detector.getDetectedCharset();

        /*預設ANSI*/
        if(charset == null){
            charset = "Cp1252";
        }

        return charset;
    }


    /**
     * 尋找資料，模糊查詢
     * @param filePath 目標檔案路徑
     * @param fileCode 檔案編碼
     * @param keyWord 查詢關鍵字
     * @return 有找到
     * @throws IOException
     */
    public static boolean searchFileContent(String filePath,String fileCode, String keyWord) throws Exception {

        if(filePath == null){throw new Exception("The function searchFileContent param [filePath] can't null ");}
        if(fileCode == null){throw new Exception("The function searchFileContent param [fileCode] can't null ");}
        if(keyWord == null){throw new Exception("The function searchFileContent param [keyWord] can't null ");}

        keyWord = keyWord.toUpperCase();
        /*去除空格*/
        keyWord = keyWord.replaceAll("\\s*", "");

        boolean isFind = false;

        BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(filePath), fileCode));

        List<String> lines ;

        lines = br.lines().collect(Collectors.toList());

        for (String line : lines) {
            line = line.toUpperCase();
            /*去除空格*/
            line = line.replaceAll("\\s*", "");

            if(line.indexOf(keyWord.toUpperCase()) != -1){
                isFind = true;
                break;
            }
        }
        return isFind;
    }

    /**
     * 轉碼程式
     * @param fileSourceDir 檔案來源
     * @param goalFileDir 存放檔案目標路徑
     * @param code 原檔編碼
     * @param trunEncode 目標編碼
     * @throws IOException
     */
    public static void convertFileCode(String fileSourceDir, String goalFileDir,String code,String trunEncode) throws IOException {

        String keyWord = "VARCHAR(";
        keyWord = keyWord.toUpperCase();
        /*去除空格*/
        keyWord = keyWord.replaceAll("\\s*", "");

        BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(fileSourceDir), code));

        PrintWriter pw = new PrintWriter( new OutputStreamWriter(new FileOutputStream(goalFileDir), trunEncode));

        String line = br.readLine();
        StringBuilder rLine;

        while (line != null) {

            String fixSt = line;
            fixSt = fixSt.replace("\t","\\s");
            System.out.println("fixSt = "+fixSt);

            rLine = new StringBuilder();
            rLine.append(spicalReplace1(line,keyWord,4));
            rLine.append("\r\n");

            pw.write(rLine.toString());
            line = br.readLine();
        }

        pw.flush();
        pw.close();

    }

    /**
     * 字串特殊處理
     * @param sourceSt 來源字串
     * @param keyWord 關鍵值
     * @param multiple 倍數
     * @return 處理完的字串
     */
    private static String spicalReplace1(String sourceSt,String keyWord,Integer multiple){
        String returnVal = "";
        StringBuilder rLine = new StringBuilder();
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);

        String fixSt = sourceSt;
        fixSt = fixSt.replace("\t","\\s");
        System.out.println("fixSt = "+fixSt);

        String[] compareLineL = fixSt.split("\\s");


        for(String compareLineI:compareLineL){
            if(compareLineI.indexOf(keyWord.toUpperCase()) != -1){

                Matcher m = p.matcher(compareLineI);

                System.out.println("compareLineI = "+compareLineI);
                String oInt = m.replaceAll("").trim();
                String nInt = Integer.toString((Integer.parseInt(m.replaceAll("").trim())*multiple));
                System.out.println("O = "+compareLineI);
                System.out.println("N = "+compareLineI.replace(oInt,nInt));
                rLine.append(compareLineI.replace(oInt,nInt));
            }
            else{
                rLine.append(compareLineI);
            }
            rLine.append("\t");
        }

        return returnVal;
    }
}
