package com.apk.franckadjibao.bluetoothtry.DataBase;

/**
 * Created by Franck ADJIBAO on 30/06/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;


public class MessageDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "msg_database";
    public static final int DB_VERSION = 1;

    public static final String MESSAGE_TABLE = "message_table";

    public static final String MESSAGE_TABLE_ID = "_id";
    public static final String MESSAGE_TABLE_MSG_CONTENT = "msg_content";
    public static final String MESSAGE_TABLE_MSG_AUTHOR = "msg_author";
    public static final String MESSAGE_TABLE_MSG_DATE = "msg_date";
    public static final String MESSAGE_TABLE_EXTERN_MAC = "extern_MAC";
    public static final String MESSAGE_TABLE_CREATE = "create table "+MESSAGE_TABLE+"("+MESSAGE_TABLE_ID+" integer primary key autoincrement, " +
            MESSAGE_TABLE_MSG_CONTENT+" text," +
            MESSAGE_TABLE_MSG_AUTHOR+"  text," +
            MESSAGE_TABLE_MSG_DATE+" text," +
            MESSAGE_TABLE_EXTERN_MAC+" text)";

    public Cursor getAdresseMACs(){
        String query = "SELECT DISTINCT extern_MAC FROM " + MESSAGE_TABLE;
        return myDatabase.rawQuery(query, null);
    }
    public Cursor getInfoWithMAc(String adresseMac){
        String query= "SELECT * FROM " + MESSAGE_TABLE + " WHERE "+MESSAGE_TABLE_EXTERN_MAC+"='" +adresseMac+"' ORDER BY "+MESSAGE_TABLE_ID+" DESC LIMIT 1";
        return myDatabase.rawQuery(query,null);
    }
    public Cursor getAllMessageForMAc(String adresseMac){

        String query = "SELECT * FROM "+MESSAGE_TABLE+" WHERE "+MESSAGE_TABLE_EXTERN_MAC+"='"+adresseMac+"'";
        return myDatabase.rawQuery(query, null);

    }
    public Cursor getRemoteDeviceName(String adresseMAC){
        String query= "SELECT "+MESSAGE_TABLE_MSG_AUTHOR+" FROM " + MESSAGE_TABLE + " WHERE "+MESSAGE_TABLE_EXTERN_MAC+"='" +adresseMAC+"' AND "+MESSAGE_TABLE_MSG_AUTHOR+" != 'moi' LIMIT 1";
        return myDatabase.rawQuery(query,null);
    }
    public void insertMessage(String message_content,String author,String adresseMac){
        Date date=new Date();
        ContentValues values= new ContentValues();
        values.put(MESSAGE_TABLE_MSG_CONTENT,message_content);
        values.put(MESSAGE_TABLE_MSG_AUTHOR,author);
        values.put(MESSAGE_TABLE_MSG_DATE,date.toString());
        values.put(MESSAGE_TABLE_EXTERN_MAC,adresseMac);
        myDatabase.insert(MESSAGE_TABLE,null,values);
    }

    String DB_PATH = null;
    private SQLiteDatabase myDatabase;
    private Context myContext;

    public MessageDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.myContext = context;

        this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.e("Path 1", DB_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //myDatabase.execSQL(MESSAGE_TABLE_CREATE);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist){

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            }catch (IOException e){
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }catch (SQLiteException e){
            e.printStackTrace();
        }
        if(checkDB != null){
            checkDB.close();
        }

        return (checkDB != null) ? true:false;
    }

    private void copyDataBase() throws IOException{
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];

        int lenght;
        while ((lenght = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, lenght);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized  void close(){
        if(myDatabase != null){
            myDatabase.close();
        }
        super.close();
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            try{
                copyDataBase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

