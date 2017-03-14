package com.group.neusoft.moviesurfer.ofj.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.ofj.collectionDBSchema.CollectionBaseHelper;
import com.group.neusoft.moviesurfer.ofj.collectionDBSchema.CollectionDbSchema;
import com.group.neusoft.moviesurfer.ofj.collectionDBSchema.HistoryBaseHelper;
import com.group.neusoft.moviesurfer.ofj.collectionDBSchema.HistoryDbSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * 17/3/13 by ofj
 * 使用：
 * 1. 建立收藏功能数据库表 ：FilmCollection.get(getApplication(),FilmCollection.TYPE_COLLECTION)
 *    建立历史记录数据库表 ：FilmCollection.get(getApplication(),FilmCollection.TYPE_HISTORY)
 * 2. FilmCollection.get(getApplication(),type).addCollection(FilmInfo filmInfo):void
 *    FilmCollection.get(getApplication(),type).deleteCollection(String url):void
 *    FilmCollection.get(getApplication(),type).getCollection(String url):FilmInfo
 *    FilmCollection.get(getApplication(),type).updateCollection(FilmInfo filmInfo):void
 *    FilmCollection.get(getApplication(),type).getCollections():List<FilmInfo>
 */
public class FilmCollection {
    public static final String TYPE_COLLECTION = "collection";
    public static final String TYPE_HISTORY = "history";
    private static FilmCollection sFilmCollection;
    private static String table;
    private static String table_url;
    private static List<FilmInfo> history_collection = new ArrayList<>();
    private static int history_tag = 0;
    private static final int MAX_HISTORY = 100;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static FilmCollection get(Context context,String type) {
        if (sFilmCollection == null) {
            sFilmCollection = new FilmCollection(context,type);
        }
        return sFilmCollection;
    }

    private FilmCollection(Context context,String type) {
        mContext = context.getApplicationContext();
        if(type == TYPE_COLLECTION) {
            mDatabase = new CollectionBaseHelper(mContext)
                    .getWritableDatabase();
            table = CollectionDbSchema.CollectionTable.NAME;
            table_url = CollectionDbSchema.CollectionTable.Cols.URL;
        }else if(type == TYPE_HISTORY){
            mDatabase = new HistoryBaseHelper(mContext)
                    .getWritableDatabase();
            table = HistoryDbSchema.HistoryTable.NAME;
            table_url = HistoryDbSchema.HistoryTable.Cols.URL;
        }else {
        }
    }


    /**
     * 添加收藏
     * @param filmInfo
     */
    public void addCollection(FilmInfo filmInfo) {
        if(table == CollectionDbSchema.CollectionTable.NAME){
            if(history_collection.size() <= MAX_HISTORY){
                history_collection.add(filmInfo);
            }else {
                mDatabase.delete(CollectionDbSchema.CollectionTable.NAME,
                        CollectionDbSchema.CollectionTable.Cols.URL+ " = ?",
                        new String[]{history_collection.get(history_tag).getUrl()});
                history_tag++;
            }
        }
        ContentValues values = getContentValues(filmInfo);
        mDatabase.insert(table, null, values);
    }

    /**
     * 删除
     * @param url
     */
    public void deleteCollection(String url){
        mDatabase.delete(table,
                table_url + " = ?",
                new String[] {url}
        );
    }
    /**
     * 通过url获取该收藏
     * @param url
     * @return
     */
    public FilmInfo getCollection(String url) {
        CollectionCursorWrapper cursor = queryCollections(
                table_url + " = ?",
                new String[] {url}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCollection();
        } finally {
            cursor.close();
        }
    }

    /**
     * 更新收藏
     * @param filmInfo
     */
    public void updateCollection(FilmInfo filmInfo) {
        String url = filmInfo.getUrl();
        ContentValues values = getContentValues(filmInfo);
        mDatabase.update(table, values,
                table_url + " = ?",
                new String[]{url});
    }

    /**
     * 获取所有收藏
     * @return
     */
    public List<FilmInfo> getCollections() {
        CollectionCursorWrapper cursor = queryCollections(null,null);
        List<FilmInfo> collections = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                collections.add(cursor.getCollection());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return collections;
    }

    private static ContentValues getContentValues(FilmInfo filmInfo) {
        ContentValues values = new ContentValues();
        values.put(CollectionDbSchema.CollectionTable.Cols.COVERIMGURL, filmInfo.getCoverImgUrl());
        values.put(CollectionDbSchema.CollectionTable.Cols.TITLE, filmInfo.getTitle());
        values.put(CollectionDbSchema.CollectionTable.Cols.DATE, filmInfo.getDate());
        values.put(CollectionDbSchema.CollectionTable.Cols.DOWNLOADURLS, filmInfo.getDownloadUrls());
        values.put(CollectionDbSchema.CollectionTable.Cols.EXTRA1, filmInfo.getExtra1());
        values.put(CollectionDbSchema.CollectionTable.Cols.EXTRA2, filmInfo.getExtra2());
        values.put(CollectionDbSchema.CollectionTable.Cols.SCOREINFO, filmInfo.getScoreInfo());
        values.put(CollectionDbSchema.CollectionTable.Cols.URL, filmInfo.getUrl());
        return values;
    }

    private CollectionCursorWrapper queryCollections(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                table,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new CollectionCursorWrapper(cursor);
    }

}
