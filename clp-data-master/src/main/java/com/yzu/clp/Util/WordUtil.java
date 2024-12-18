package com.yzu.clp.Util;

import com.deepoove.poi.XWPFTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Map;

/**
 * @author Jerry
 * @Title: WordUtil
 * @Description: Word工具类
 * @date 219/10/6 9:09
 */
public class WordUtil {

    private static Logger logger = LoggerFactory.getLogger(WordUtil.class);


    /**
     * 根据模板填充内容生成word
     * 调用方法参考下面的main方法，详细文档参考官方文档
     * Poi-tl模板引擎官方文档：http://deepoove.com/poi-tl/
     *
//     * @param templatePath word模板文件路径
     * @param fileDir      生成的文件存放地址
     * @param fileName     生成的文件名,不带格式。假如要生成abc.docx，则fileName传入abc即可
     * @param paramMap     替换的参数集合
     * @return 生成word成功返回生成的文件的路径，失败返回空字符串
     */
//    public static String createWord(String templatePath, String fileDir, String fileName, Map<String, Object> paramMap) {
//        Assert.notNull(templatePath, "word模板文件路径不能为空");
//        Assert.notNull(fileDir, "生成的文件存放地址不能为空");
//        Assert.notNull(fileName, "生成的文件名不能为空");
//
//        // 生成的word格式
//        String formatSuffix = ".docx";
//        // 拼接后的文件名
//        fileName = fileName + formatSuffix;
//
//        // 生成的文件的存放路径
//        if (!fileDir.endsWith("/")) {
//            fileDir = fileDir + File.separator;
//        }
//
//        File dir = new File(fileDir);
//        if (!dir.exists()) {
//            logger.info("生成word数据时存储文件目录{}不存在,为您创建文件夹!", fileDir);
//            dir.mkdirs();
//        }
//
//        String filePath = fileDir + fileName;
//        // 读取模板templatePath并将paramMap的内容填充进模板，即编辑模板+渲染数据
//        XWPFTemplate template = XWPFTemplate.compile(templatePath).render(paramMap);
//        try {
//            // 将填充之后的模板写入filePath
//            template.writeToFile(filePath);
//            template.close();
//        } catch (Exception e) {
//            logger.error("生成word异常", e);
//            e.printStackTrace();
//            return "";
//        }
//        return filePath;
//    }

    public static String createWord(String templateStream, String fileDir, String fileName, Map<String, Object> paramMap) {
        Assert.notNull(templateStream, "word模板文件路径不能为空");
        Assert.notNull(fileDir, "生成的文件存放地址不能为空");
        Assert.notNull(fileName, "生成的文件名不能为空");

        // 生成的word格式
        String formatSuffix = ".docx";
        // 拼接后的文件名
        fileName = fileName + formatSuffix;

        // 生成的文件的存放路径
        if (!fileDir.endsWith("/")) {
            fileDir = fileDir + File.separator;
        }

        File dir = new File(fileDir);
        if (!dir.exists()) {
            logger.info("生成word数据时存储文件目录{}不存在,为您创建文件夹!", fileDir);
            dir.mkdirs();
        }

        String filePath = fileDir + fileName;
        // 读取模板templatePath并将paramMap的内容填充进模板，即编辑模板+渲染数据
        XWPFTemplate template = XWPFTemplate.compile(templateStream).render(paramMap);
        try {
            // 将填充之后的模板写入filePath
            template.writeToFile(filePath);
            template.close();
        } catch (Exception e) {
            logger.error("生成word异常", e);
            e.printStackTrace();
            return "";
        }
        return filePath;
    }

}
