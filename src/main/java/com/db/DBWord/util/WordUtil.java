/**  
 * @Title WordUtil.java
 * @Package com.db.DBWord.util
 * @author 侯文远
 * @date 2016年11月18日 下午4:10:13
 * @version V1.0
 * Copyright © 个人所有
 */
package com.db.DBWord.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <B>概要说明：</B>freemarker模板合成工具类<BR>
 * 
 * @author 侯文远
 * @date 2016年11月18日 下午4:10:13
 */
public class WordUtil {
    /**
     * <B>方法名称：</B> createWord<BR>
     * <B>概要说明：</B> 合成数据<BR>
     * 
     * @param dataMap 模板数据
     * @param templateName 模板名称
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @author 侯文远
     * @create 2016年11月18日 下午4:12:29
     */
    @SuppressWarnings("deprecation")
    public static void createWord(Map<String, Object> dataMap, String templateName, String filePath, String fileName) {
        try {
            // 创建配置实例
            Configuration configuration = new Configuration();
            
            // 设置编码
            configuration.setDefaultEncoding("UTF-8");
            
            // ftl模板文件统一放至 com.lun.template 包下面
            configuration.setClassForTemplateLoading(WordUtil.class, "/com/db/DBWord/template/");
            
            // 获取模板
            Template template = configuration.getTemplate(templateName);
            
            // 输出文件
            File outFile = new File(filePath + File.separator + fileName);
            
            // 如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            
            // 将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
            
            // 生成文件
            template.process(dataMap, out);
            
            // Thread.sleep(5000);
            
            // 关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
