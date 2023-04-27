package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsapp.Models.Headlines;
import com.example.newsapp.Models.SavedNewsHeadlines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME="news_portal";
    public DBHelper(Context context){
        super(context, "news_portal", null, 2);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(id INTEGER primary key NOT NULL, name TEXT NOT NULL, email TEXT unique NOT NULL, password TEXT NOT NULL)");
        db.execSQL("create table saved_news(id INTEGER primary key NOT NULL,user_id INTEGER NOT NULL, title TEXT NOT NULL, source TEXT, image TEXT,url TEXT,author TEXT,detail TEXT,content TEXT,time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists saved_news");
        onCreate(db);
    }

    public Boolean insertData(String name, String email,String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        long result= db.insert("users", null,values);
        return result != -1;
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=?", new String[] {email});
        return cursor.getCount() > 0;
    }

    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=? and password=?", new String[] {email,password});
        return cursor.getCount() > 0;
    }

    @SuppressLint("Range")
    public String getName(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name from users where email = ?",new String[] {email});
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("name"));
        }
        return null;
    }

    @SuppressLint("Range")
    public int getUserId(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select id from users where email = ?",new String[] {email});
        if(cursor != null){
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("id"));
        }
        return 0;
    }

    public Boolean insertNews(Integer user_id,String title, String source, String image,String author,String url,String detail, String content,String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", user_id);
        values.put("title", title);
        values.put("source", source);
        values.put("image", image);
        values.put("author", author);
        values.put("url",url);
        values.put("detail", detail);
        values.put("content", content);
        values.put("time", time);

        long result= db.insert("saved_news", null,values);
        Cursor cursor = db.rawQuery("select * from saved_news where user_id=?", new String[] {user_id.toString()});

        return result != -1;
    }

    public Boolean checkNews(String title,Integer user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from saved_news where title=? and user_id=?", new String[] {title,user_id.toString()});
        return cursor.getCount() > 0;
    }

    public int deleteNews(Integer user_id, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("saved_news","user_id=? and title=?",new String[] {user_id.toString(),title});
    }

    @SuppressLint("Range")
    public ArrayList<SavedNewsHeadlines> getNewsList(Integer user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from saved_news where user_id=?", new String[] {user_id.toString()});

        ArrayList<SavedNewsHeadlines> headlinesList = new ArrayList<SavedNewsHeadlines>();
        // moving our cursor to first position.
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
           headlinesList.add(new SavedNewsHeadlines(
                    cursor.getString(cursor.getColumnIndex("source")),
                    cursor.getString(cursor.getColumnIndex("author")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("detail")),
                    cursor.getString(cursor.getColumnIndex("url")),
                    cursor.getString(cursor.getColumnIndex("image")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("content"))
                    )
            );
            cursor.moveToNext();
        }
        return headlinesList;
    }
}

