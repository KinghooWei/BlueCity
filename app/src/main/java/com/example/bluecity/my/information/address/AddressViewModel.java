package com.example.bluecity.my.information.address;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

public class AddressViewModel extends AndroidViewModel {
    private SavedStateHandle handle;
    MutableLiveData<String> province, city, community, building, roomId, finalCommunity, finalBuilding;
    MutableLiveData<Boolean> isFromBuilding;

    public AddressViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
        if (!handle.contains("community") || !handle.contains("building") || !handle.contains("roomId")) {
            load();
        }

        province = new MutableLiveData<>();

        city = new MutableLiveData<>();

        community = new MutableLiveData<>();

        building = new MutableLiveData<>();

        roomId = new MutableLiveData<>();

        finalCommunity = new MutableLiveData<>();
        finalBuilding = new MutableLiveData<>();

        isFromBuilding = new MutableLiveData<>();
        isFromBuilding.setValue(false);
    }

    private void load() {
        SharedPreferences shp = getApplication().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String community = shp.getString("community", null);
        String building = shp.getString("building", null);
        String roomId = shp.getString("roomId", null);
        handle.set("community", community);
        handle.set("building", building);
        handle.set("roomId", roomId);
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

    public MutableLiveData<String> getProvince() {
        return province;
    }

    public MutableLiveData<String> getCity() {
        return city;
    }

    public MutableLiveData<String> getCommunity() {
        return community;
    }

    public MutableLiveData<String> getBuilding() {
        return building;
    }

    public MutableLiveData<Boolean> getIsFromBuilding() {
        return isFromBuilding;
    }

    public MutableLiveData<String> getFinalBuilding() {
        return finalBuilding;
    }

    public MutableLiveData<String> getFinalCommunity() {
        return finalCommunity;
    }
}
