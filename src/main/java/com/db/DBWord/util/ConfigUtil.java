/**  
 * @Title ConfigUtil.java
 * @Package com.db.DBWord.util
 * @author 侯文远
 * @date 2016年11月21日 下午5:33:01
 * @version V1.0
 * Copyright © 个人所有
 */
package com.db.DBWord.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * <B>概要说明：</B>properties类加载工具类<BR>
 * 
 * @author 侯文远
 * @date 2016年11月21日 下午5:33:01
 */
public abstract class ConfigUtil {
    
    public static Properties prop;
    
    /**
     * <B>方法名称：</B> readValue<BR>
     * <B>概要说明：</B> 获取配置文件中的某个字符串类型的值<BR>
     * 
     * @param propertiesName 配置文件
     * @param key 主键
     * @return
     * @author 侯文远
     * @create 2016年11月21日 下午5:46:36
     */
    public static String readValue(String propertiesName, String key) {
        if (StringUtils.isBlank(propertiesName) || StringUtils.isBlank(key)) {
            throw new NullPointerException("propertiesName or  key is null");
        }
        ResourceBundle bundle = ResourceBundle.getBundle("propertiesName");
        return bundle.getString(key);
        
    }
    
    /**
     * <B>方法名称：</B> readValue<BR>
     * <B>概要说明：</B> 获取配置文件中的某个字符串类型的值<BR>
     * 如果key不存在，返回传递的默认值
     * 
     * @param propertiesName properties文件名
     * @param key 键
     * @param defaultValue 默认值
     * @return
     * @author 侯文远
     * @create 2016年11月21日 下午5:48:31
     */
    public static String readValue(String propertiesName, String key, String defaultValue) {
        if (StringUtils.isBlank(propertiesName) || StringUtils.isBlank(key)) {
            throw new NullPointerException("propertiesName or key is null");
        }
        ResourceBundle bundle = ResourceBundle.getBundle(propertiesName);
        return bundle.getString(key) == null ? defaultValue : bundle.getString(key);
        
    }
    
    /**
     * <B>方法名称：</B> initProp<BR>
     * <B>概要说明：</B> 初始化properties流<BR>
     * 
     * @param propertiesName
     * @author 侯文远
     * @create 2016年11月22日 上午11:26:09
     */
    public static void initProp(String propertiesName) {
        try {
            prop = new Properties();
            FileInputStream inputStream = new FileInputStream(propertiesName);
            prop.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * <B>方法名称：</B> getPropValue<BR>
     * <B>概要说明：</B> 获取配置文件中的某个字符串类型的值<BR>
     * 
     * @param propertiesName
     * @param key
     * @return
     * @author 侯文远
     * @create 2016年11月22日 上午10:34:39
     */
    public static String getPropValue(String key) {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key is null");
        }
        return prop.getProperty(key);
    }
    
    /**
     * <B>方法名称：</B> getPropValue<BR>
     * <B>概要说明：</B> 获取配置文件中的某个字符串类型的值<BR>
     * 如果key不存在，返回传递的默认值
     * 
     * @param propertiesName
     * @param key
     * @param defaultValue
     * @return
     * @author 侯文远
     * @create 2016年11月22日 上午10:34:56
     */
    public static String getPropValue(String key, String defaultValue) {
        if (StringUtils.isBlank(key) && StringUtils.isBlank(defaultValue)) {
            throw new NullPointerException("key and defaultValue all for null");
        }
        return prop.getProperty(key, defaultValue);
    }
    
}
