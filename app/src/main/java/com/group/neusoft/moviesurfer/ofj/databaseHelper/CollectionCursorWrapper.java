package com.group.neusoft.moviesurfer.ofj.databaseHelper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.group.neusoft.moviesurfer.FilmInfo;
import com.group.neusoft.moviesurfer.ofj.collectionDBSchema.CollectionDbSchema;



public class CollectionCursorWrapper extends CursorWrapper {
    public CollectionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public FilmInfo getCollection() {
        String url = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.URL));
        String title = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.TITLE));
        String date = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.DATE));
        String coverimgurl = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.COVERIMGURL));
        String dowmloadurls = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.DOWNLOADURLS));
        String extra1 = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.EXTRA1));
        String extra2 = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.EXTRA2));
        String scoreinfo = getString(getColumnIndex(CollectionDbSchema.CollectionTable.Cols.SCOREINFO));
        FilmInfo filmInfo = new FilmInfo();
        filmInfo.setTitle(title);
        filmInfo.setDate(date);
        filmInfo.setCoverImgUrl(coverimgurl);
        filmInfo.setDownloadUrls(dowmloadurls);
        filmInfo.setExtra1(extra1);
        filmInfo.setExtra2(extra2);
        filmInfo.setScoreInfo(scoreinfo);
        filmInfo.setUrl(url);
        return filmInfo;
    }
}
