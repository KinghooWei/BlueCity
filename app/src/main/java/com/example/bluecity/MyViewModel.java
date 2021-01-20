package com.example.bluecity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

public class MyViewModel extends AndroidViewModel {
    private SavedStateHandle handle;

    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
        if (!handle.contains("phoneNum") || !handle.contains("loginPwd")) {
            load();
        }
    }

    private void load() {
        SharedPreferences shp = getApplication().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String phoneNum = shp.getString("phoneNum", null);
        String loginPwd = shp.getString("loginPwd", null);
        handle.set("phoneNum", phoneNum);
        handle.set("loginPwd", loginPwd);
    }

    void save(String key, String info) {
        SharedPreferences shp = getApplication().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString(key, info);
        editor.apply();
    }

    LiveData<String> getInfo(String info) {
        return handle.getLiveData(info);
    }
}
