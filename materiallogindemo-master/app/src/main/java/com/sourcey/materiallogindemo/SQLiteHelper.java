package com.sourcey.materiallogindemo;

/**
 * Created by Juned on 3/13/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="UserDataBase.db";

    public static final String TABLE_NAME="UserTable";

    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Name="name";

    public static final String Table_Column_2_Email="email";

    public static final String Table_Column_3_Password="password";

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {


        String CREATE_TABLE="create table users " +
                "(id integer primary key, name text,email text)";
        database.execSQL(CREATE_TABLE);

    }
    public boolean insertUser(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        db.insert("users", null, contentValues);
        return true;
    }
    public boolean isUserPresent(String email) {
        boolean flag= false;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from users where email="+"'"+email+"'", null );
            if(res.getCount() <= 0){
                res.close();
                return false;
            }
            res.close();
            return true;
        }catch (Exception e){
            flag = false;
        }
        return flag ;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
}
