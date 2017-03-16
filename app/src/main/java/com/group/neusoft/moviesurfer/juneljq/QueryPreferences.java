package com.group.neusoft.moviesurfer.juneljq;

import android.content.Context;
import android.preference.PreferenceManager;


public class QueryPreferences {
    private static final String NEW_FILM = "recrive_new_film";

    public static String getNewFilm(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(NEW_FILM, null);
    }
    public static void setNewFilm(Context context, String film_name) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(NEW_FILM, film_name)
                .apply();
    }
}
