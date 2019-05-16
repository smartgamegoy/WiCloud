package com.jetec.wicloud.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class DeviceList extends SQLiteOpenHelper {

    private String table_name = "newDevice"; //資料表名
    private static String db_name = "DeviceSQL.db";    //資料庫名
    private static int VERSION = 2;

    public DeviceList(Context context) {
        super(context, db_name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //ok
        String DATABASE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + "(" +
                "id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," +
                "usersave" + " TEXT" + ")";
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        db.execSQL("DROP TABLE IF EXISTS " + table_name); //刪除舊有的資料表
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打開數據庫後首先被執行
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name, null);
        return cursor.getCount();
    }

    public void insert(String json){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("usersave", json);

        db.insert(table_name, json, cv);
    }

    public ArrayList<HashMap<String, String>> getJSON(){

        ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
        dataList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name, null);
        cursor.moveToFirst();
        if(cursor.getCount() != 0) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String json = cursor.getString(cursor.getColumnIndex("usersave"));
                HashMap<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("json", json);
                dataList.add(map);
            } while (cursor.moveToNext());
        }
        return dataList;
    }

    public boolean update(int id, String json){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("usersave", json);

        long set_update = db.update(table_name, cv, "id=" + id, null);
        return set_update > 0;
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, "id=" + id ,null);
    }
}
