/**  
 * @Title DBSyncWord.java
 * @Package com.db.DBWord
 * @author 侯文远
 * @date 2016年11月18日 下午3:56:55
 * @version V1.0
 * Copyright © 个人所有
 */
package com.db.DBWord.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.db.DBWord.constant.Constant;
import com.db.DBWord.entity.Table;
import com.db.DBWord.entity.TableColumn;
import com.db.DBWord.util.ConfigUtil;
import com.db.DBWord.util.WordUtil;

/**
 * <B>概要说明：</B>同步数据库文档<BR>
 * 
 * @author 侯文远
 * @date 2016年11月18日 下午3:56:55
 */
public class DBSyncWord {
    private Connection conn = null;// 数据库连接
    
    private String url;// 用户名
    
    private String username;// 用户名
    
    private String password;// 密码
    
    private String tableSchema;// 数据库名
    
    private String filePath;// 文件路径
    
    private String fileOnlyName;// 文件唯一名称
    
    /**
     * @title DBSyncWord
     * @description:
     * @author 侯文远
     * @create 2016年11月22日 上午11:19:29
     */
    public DBSyncWord() {
        
    }
    
    /**
     * @title DBSyncWord
     * @description:构造函数
     * @author 侯文远
     * @param args
     * @create 2016年11月21日 下午6:00:15
     */
    public DBSyncWord(String args) {
        ConfigUtil.initProp(args + "\\conf\\dbword.properties");
        this.url = ConfigUtil.getPropValue("URL");
        this.tableSchema = ConfigUtil.getPropValue("TABLE_SCHEMA");
        this.username = ConfigUtil.getPropValue("USER_NAME");
        this.password = ConfigUtil.getPropValue("PASSWORD");
        this.filePath = ConfigUtil.getPropValue("FILE_PATH", args + "\\docment");
        this.fileOnlyName = ConfigUtil.getPropValue("FILE_ONLY_NAME", "数据库文档");
    }
    
    /**
     * <B>方法名称：</B> getAllTables<BR>
     * <B>概要说明：</B> 获取所有表信息<BR>
     * 
     * @author 侯文远
     * @return
     * @create 2016年11月18日 下午4:48:39
     */
    public List<Table> getAllTables() {
        conn = getconConnection();// 获取数据库连接
        Statement stmt = null;
        try {
            DatabaseMetaData odmd = conn.getMetaData();
            stmt = conn.createStatement();
            
            // -------------------START-rs--------------------------------
            ResultSet rs = odmd.getTables(null, null, "", new String[] {"TABLE"});// 获取所有表信息
            List<Table> result = new ArrayList<Table>();
            int i = 0;
            while (rs.next()) {
                String tableName = rs.getString(3);// 获取表名
                Table table = new Table();
                table.setTableName(tableName);
                table.setTableComment("");// 防止空指针
                
                i++;
                System.out.println(i + "--" + tableName);
                
                String sqlObject = "SHOW CREATE TABLE " + tableSchema + "." + tableName;
                // -------------------START-rSet--------------------------------
                ResultSet rSet = stmt.executeQuery(sqlObject);
                while (rSet.next()) {
                    String createSQL = rSet.getString(Constant.SQLOBJ_TABLE_CH) + Constant.END;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(createSQL.getBytes())));
                    String str = "";
                    int lineNum = 0;
                    int endLineNum = 0;
                    List<TableColumn> columns = new ArrayList<TableColumn>();
                    while ((str = reader.readLine()) != null) {
                        if ((str.trim().startsWith("PRIMARY KEY") || str.trim().startsWith(")") || str.trim().startsWith("KEY")) && endLineNum == 0) {
                            endLineNum = lineNum;
                        }
                        lineNum++;
                        if (lineNum >= 2 && endLineNum == 0) {
                            System.out.println(str);
                            String columnName = str.split("`")[1];
                            String columnType = str.split(" ")[3];
                            TableColumn column = new TableColumn();
                            column.setColumnName(columnName);
                            column.setColumnType(columnType);
                            
                            if (str.contains("COMMENT '")) {
                                String comment = str.substring(str.indexOf("COMMENT '") + 9, str.lastIndexOf("'"));
                                String defaultStr = str.substring(str.indexOf(columnType) + columnType.length(), str.indexOf(" COMMENT"));
                                column.setComment(comment);
                                column.setDefaultStr(defaultStr);
                            } else {
                                String defaultStr = str.substring(str.indexOf(columnType) + columnType.length());
                                column.setDefaultStr(defaultStr);
                            }
                            columns.add(column);
                        }
                        if (str.trim().startsWith("PRIMARY KEY")) {
                            table.setPrimaryKey(str);
                        }
                        if (str.trim().startsWith("KEY")) {
                            table.setIndex(str);
                        }
                        if (str.trim().startsWith(")")) {
                            table.setOthers(str);
                        }
                        if (str.trim().contains("COMMENT='")) {// 获取表注释
                            String tableComment = str.substring(str.indexOf("COMMENT='") + 9, str.lastIndexOf("';"));
                            table.setTableComment(tableComment);
                        }
                    }
                    table.setColumns(columns);
                }
                
                Map<String, String> params = new HashMap<String, String>();
                params.put("tableSchema", tableSchema);
                params.put("tableName", tableName);
                // List<String> triggerNameList = syncMapper.selectTableTrigger(params);
                // if(triggerNameList!=null && triggerNameList.size()>0){
                // String triggerSql = "";
                // for(String triggerName : triggerNameList){
                // SqlObject sqlObjectTrigger = new SqlObject(tableSchema, Constant.SQLOBJ_TRIGGER, triggerName);
                // String createSQLTrigger =
                // syncMapper.selectCreateSqlObj(sqlObjectTrigger).get(Constant.SQLOBJ_TRIGGER_CH) + end;
                // createSQLTrigger = createSQLTrigger.replaceAll(definerReg, "");
                // triggerSql+= createSQLTrigger+Constant.SIGN_NEWLINE;
                // }
                // table.setTrigger(triggerSql);
                // }
                result.add(table);
                rSet.close();// 110
                // -------------------END-rSet--------------------------------
            }
            rs.close();// 98
            // -------------------END-rs--------------------------------
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public void createWord(List<Table> newsList) {
        /** 文件名称，唯一字符串 */
        Random r = new Random();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        StringBuffer sb = new StringBuffer();
        sb.append(sdf1.format(new Date()));
        sb.append("_");
        sb.append(r.nextInt(100));
        
        /** 用于组装word页面需要的数据 */
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("tableList", newsList);
        
        /** 生成word */
        WordUtil.createWord(dataMap, "db_docment.ftl", filePath, fileOnlyName + "_" + sb + ".doc");
    }
    
    /**
     * <B>方法名称：</B> getconConnection<BR>
     * <B>概要说明：</B> 获取数据库地址<BR>
     * 
     * @return conn连接
     * @author 侯文远
     * @create 2016年11月18日 下午5:04:47
     */
    public Connection getconConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url + "/" + tableSchema, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
