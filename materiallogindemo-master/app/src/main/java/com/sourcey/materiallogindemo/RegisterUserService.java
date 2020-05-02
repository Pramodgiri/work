package com.sourcey.materiallogindemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class RegisterUserService {

    EditText Email, Password, Name ;
    Button Register;
    String nameHolder, emailHolder, passwordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";

    // Adding click listener to register button.
    public void saveUserDetails(String name,String email,String password) {
        this.nameHolder = name;
        this.emailHolder = email;
        this.passwordHolder = password;
        // Creating SQLite database if dose n't exists
        SQLiteDataBaseBuild();

        // Creating SQLite table if dose n't exists.
        SQLiteTableBuild();

        // Checking EditText is empty or Not.
        CheckEditTextStatus();

        // Method to check Email is already exists or not.
        CheckingEmailAlreadyExistsOrNot();

        // Empty EditText After done inserting process.
        EmptyEditTextAfterDataInsert();
        Log.d("Message","Doneeeee");

    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        File file =  new File(SQLiteHelper.DATABASE_NAME);
        sqLiteDatabaseObj = SQLiteDatabase.openOrCreateDatabase(file.getPath(), null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" +
                SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Email +
                " VARCHAR, " + SQLiteHelper.Table_Column_3_Password + " VARCHAR);");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase(){
        // SQLite query to insert data into table.
        SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" " +
                "(name,email,password) VALUES('"+nameHolder+"', '"+emailHolder+"', '"+passwordHolder+"');";

        // Executing query.
        sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

        // Closing SQLite database object.
        sqLiteDatabaseObj.close();
    }

    // Empty edittext after done inserting process method.
    public void EmptyEditTextAfterDataInsert(){

        Name.getText().clear();

        Email.getText().clear();

        Password.getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        nameHolder = Name.getText().toString() ;
        emailHolder = Email.getText().toString();
        passwordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(passwordHolder)){

            EditTextEmptyHolder = false ;

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    // Checking Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot(){

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " +
                SQLiteHelper.Table_Column_2_Email + "=?", new String[]{emailHolder}, null, null, null);
        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Email is already exists then Result variable value set as Email Found.
                F_Result = "Email Found";

                // Closing cursor.
                cursor.close();
            }
        }
        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();

    }
    // Checking result
    public String CheckFinalResult(){
        // Checking whether email is already exists or not.
        if(F_Result.equalsIgnoreCase("Email Found"))
        {
            // If email is exists then toast msg will display.
            return "Email Already Exists";
        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();
        }
       return "Successfully Created" ;

    }
    public boolean isValidLogin(String email,String password){
        // Opening SQLite database write permission.
        boolean flag=false;
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " +
                SQLiteHelper.Table_Column_2_Email + "=?", new String[]{email}, null, null, null);
        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Email is already exists then Result variable value set as Email Found.
                flag = true;
                // Closing cursor.
                cursor.close();
            }
        }
        return flag ;
    }
}
