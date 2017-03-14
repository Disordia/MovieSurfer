package com.group.neusoft.moviesurfer.ofj.collectionDBSchema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CollectionBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "collectionBase.db";
    //private static final String DATABASE_NAME_HISTORY = "historyBase.db";

    public CollectionBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ CollectionDbSchema.CollectionTable.NAME
        +"(" + "_id integer primary key autoincrement," +
                CollectionDbSchema.CollectionTable.Cols.URL + "," +
                CollectionDbSchema.CollectionTable.Cols.TITLE + ","+
                CollectionDbSchema.CollectionTable.Cols.DATE + "," +
                CollectionDbSchema.CollectionTable.Cols.COVERIMGURL + ","+
                CollectionDbSchema.CollectionTable.Cols.DOWNLOADURLS + ","+
                CollectionDbSchema.CollectionTable.Cols.SCOREINFO + ","+
                CollectionDbSchema.CollectionTable.Cols.EXTRA1 + ","+
                CollectionDbSchema.CollectionTable.Cols.EXTRA2 +
        ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
