package com.jetec.wicloud.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserAccount extends SQLiteOpenHelper {

    private String table_name = "newAccount"; //資料表名
    private static String db_name = "AccountSQL.db";    //資料庫名
    private static int VERSION = 1;

    public UserAccount(Context context) {
        super(context, db_name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + "(" +
                "id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," +
                "account" + " TEXT, " +
                "password" + " TEXT" + ")";
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

    public void insert(String account, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("account ", account);
        cv.put("password", password);

        db.insert(table_name, account, cv);
    }

    public void update(String account, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("account ", account);
        cv.put("password", password);

        db.update(table_name, cv, null, null);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(table_name, null, null,
                null, null, null, null);
    }
}
