package com.example.bluecity.enity;

import java.util.Date;

public class Record {
    private String time;
    private String community;
    private String building;
    private byte[] scene;

    public Record(String time, String community, String building, byte[] scene) {
        this.time = time;
        this.community = community;
        this.building = building;
        this.scene = scene;
    }

    public String getTime() {
        return time;
    }

    public String getCommunity() {
        return community;
    }

    public String getBuilding() {
        return building;
    }

    public byte[] getScene() {
        return scene;
    }
}
