package com.tasklist.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper
{
    Context c;
    public DataBaseHelper(Context context)
    {
        super(context,"MyTaskList",null,1);
        this.c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE MyTaskList (TASK TEXT, DATE TEXT, TIME TEXT, COMPLETED TEXT,TYPE TEXT)";
        db.execSQL(createTable);
    }

    public void addTask(String task, String date, String time, String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TASK",task);
        contentValues.put("DATE",date);
        contentValues.put("TIME",time);
        contentValues.put("COMPLETED","FALSE");
        contentValues.put("TYPE",type);
        db.insert("MyTaskList",null,contentValues);
        db.close();
    }

    public void deleteTask(String task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MyTaskList","TASK=?", new String[]{task});
        db.close();
    }

    public void setCompleted(String task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPLETED","TRUE");
        db.update("MyTaskList",contentValues,"TASK=?", new String[]{task});
        db.close();
    }

    public void setPending(String task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("COMPLETED","FALSE");
        db.update("MyTaskList",contentValues,"TASK=?", new String[]{task});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists MyTaskList");
        onCreate(db);
    }
}
