package com.elegance.nssrecruitment;

import android.database.sqlite.SQLiteDatabase;



/**
 * Created by jodiwaljay on 12/7/16.
 */
public class DatabaseController {

    SQLiteDatabase db;

    public DatabaseController(String DbName){
        db = SQLiteDatabase.openOrCreateDatabase(DbName,null);
    }

    /*public void createDatabase(String DbName){
        db = SQLiteDatabase.openOrCreateDatabase(DbName,null);
    }*/

    public void createTable(String TableName, PairDataType ... colNames ){

        String tableCreateSql = "CREATE TABLE IF NOT EXISTS ";

        tableCreateSql = tableCreateSql + TableName + "(";

        for (PairDataType pair: colNames) {
            tableCreateSql = tableCreateSql + pair.getColumnName() + " " + pair.getDtType() + ",";
        }

        tableCreateSql = tableCreateSql + ");";

        db.execSQL(tableCreateSql);

    }

    public void createEntry (String TableName, String ... values){
        String entryCreateSql = "INSERT INTO " + TableName + " VALUES('" ;

        for(String value : values){
            entryCreateSql = entryCreateSql + value + "','";
        }

        entryCreateSql = entryCreateSql + "');" ;
        db.execSQL(entryCreateSql);
    }


}
