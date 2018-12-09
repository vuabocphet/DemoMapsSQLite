package com.tinh.dev.poly.maps.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class DataBase {

    private static final String DB_NAME="MAPS";
    private static final String TB_MAPS="Maps";
    private static final int DB_VERSION=1;

    private SQLiteDatabase dataBase;

    public DataBase (Context context){
        OpenHelper openHelper=new OpenHelper(context);
        dataBase=openHelper.getWritableDatabase();
    }


    public class OpenHelper extends SQLiteOpenHelper{

        public OpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String lenhtaobang="CREATE TABLE IF NOT EXISTS Maps(_id INTEGER PRIMARY KEY AUTOINCREMENT,latitude NVARCHAR,longitude NVARCHAR)";
            db.execSQL(lenhtaobang);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           db.execSQL("DROP TABLE IF EXISTS "+TB_MAPS);
        }
    }


    public void insert(String la,String lo){
        ContentValues values=new ContentValues();
        values.put("latitude",la);
        values.put("longitude",lo);
        dataBase.insert(TB_MAPS,null,values);
    }

    public void update(String la,String lo,int i){

        ContentValues values=new ContentValues();
        values.put("latitude",la);
        values.put("longitude",lo);
        dataBase.update(TB_MAPS,values,"_id="+i,null);
    }

    public void delete(int i){
     dataBase.delete(TB_MAPS,"_id="+i,null);

    }

    public Cursor getdata(){
      return   dataBase.query(TB_MAPS,null
        ,null
        ,null
        ,null
        ,null
        ,null);
    }



}
