package com.group.neusoft.moviesurfer;

/**
 * Created by ttc on 2017/3/10.
 */

public class FilmInfo {
        private String mUrl;                                    //the info page url of the film
        private String mTitle;                                  //the title string of the film
        private String mDownloadUrlsInfo;            // the film download url detail info like 'xxx[1080p]part1.rmvb'
        private String mDownloadUrls;                  //the download urls of the film split by tab or space
        private String mCoverImgUrl;                    //the cover image url
        private String mDate;                                   //the start date of the film
        private String mScoreInfo;                          //the score string like IBME 6.0/10
        private String mExtra1;                                 //for future use
        private String mExtra2;                                 //for future use

        public FilmInfo(String url, String title, String downloadUrlsInfo, String downloadUrls, String coverImgUrl, String date, String scoreInfo, String extra1, String extra2) {
                mUrl = url;
                mTitle = title;
                mDownloadUrlsInfo = downloadUrlsInfo;
                mDownloadUrls = downloadUrls;
                mCoverImgUrl = coverImgUrl;
                mDate = date;
                mScoreInfo = scoreInfo;
                mExtra1 = extra1;
                mExtra2 = extra2;
        }

        public FilmInfo() {

        }

        @Override
        public boolean equals(Object obj) {
                return this.getUrl().equals(((FilmInfo)obj).getUrl());
        }


        public String getUrl() {
                return mUrl;
        }

        public void setUrl(String url) {
                mUrl = url;
        }

        public String getTitle() {
                return mTitle;
        }

        public void setTitle(String title) {
                mTitle = title;
        }

        public String getDownloadUrls() {
                return mDownloadUrls;
        }

        public void setDownloadUrsl(String downloadUrls) {
                mDownloadUrls = downloadUrls;
        }

        public String getCoverImgUrl() {
                return mCoverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
                mCoverImgUrl = coverImgUrl;
        }

        public String getScoreInfo() {
                return mScoreInfo;
        }

        public void setScoreInfo(String scoreInfo) {
                mScoreInfo = scoreInfo;
        }

        public String getExtra1() {
                return mExtra1;
        }

        public void setExtra1(String extra1) {
                mExtra1 = extra1;
        }

        public String getExtra2() {
                return mExtra2;
        }

        public void setExtra2(String extra2) {
                mExtra2 = extra2;
        }

        public String getDate() {
                return mDate;
        }

        public void setDate(String date) {
                mDate = date;
        }


        public String getDownloadUrlsInfo() {
                return mDownloadUrlsInfo;
        }

        public void setDownloadUrlsInfo(String downloadUrlsInfo) {
                mDownloadUrlsInfo = downloadUrlsInfo;
        }

        public void setDownloadUrls(String downloadUrls) {
                mDownloadUrls = downloadUrls;
        }

        @Override
        public String toString() {
                return mUrl+";"+mTitle+";"+mCoverImgUrl+";"+mDate+";"+mExtra2;
        }
}
