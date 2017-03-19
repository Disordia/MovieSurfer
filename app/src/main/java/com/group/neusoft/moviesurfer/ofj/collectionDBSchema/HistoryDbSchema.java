package com.group.neusoft.moviesurfer.ofj.collectionDBSchema;


public class HistoryDbSchema {
    public static final class HistoryTable{
        public static final String NAME = "history";

        public static final class Cols{
            public static final String URL = "url";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DOWNLOADURLS = "download_urls";
            public static final String COVERIMGURL = "coverimgurl";
            public static final String SCOREINFO = "scoreinfo";
            public static final String EXTRA1 = "extra1";
            public static final String EXTRA2 = "extra2";
        }
    }
}
