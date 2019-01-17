package com.db.DBWord;

import java.util.List;

import com.db.DBWord.entity.Table;
import com.db.DBWord.service.DBSyncWord;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        DBSyncWord dbsw = new DBSyncWord(args[0]);
        List<Table> tables = dbsw.getAllTables();
        dbsw.createWord(tables);
    }
}
