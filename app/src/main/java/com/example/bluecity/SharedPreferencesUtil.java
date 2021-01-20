package com.example.bluecity;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private SharedPreferences shp;

    public SharedPreferencesUtil(Context context, String shPrefName) {
        shp = context.getSharedPreferences(shPrefName, Context.MODE_PRIVATE);
    }

    public void saveString(String key, String name) {
        SharedPreferences.Editor editor = shp.edit();
        editor.putString(key, name);
        editor.apply();
    }

    public void saveBoolean(String key, boolean name) {
        SharedPreferences.Editor editor = shp.edit();
        editor.putBoolean(key, name);
        editor.apply();
    }

    public void clearAll() {
        SharedPreferences.Editor editor = shp.edit();
        editor.clear();
        editor.commit();
    }

    public String loadString(String key) {
        return shp.getString(key, null);
    }

    public boolean loadBoolean(String key) {
        return shp.getBoolean(key, true);
    }
}
