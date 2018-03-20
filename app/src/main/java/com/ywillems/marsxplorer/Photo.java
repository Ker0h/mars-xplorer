package com.ywillems.marsxplorer;

import java.io.Serializable;

/**
 * Created by ywillems on 13-3-2018.
 */

public class Photo implements Serializable {

    private String id;
    private String url;
    private String camera;

    public Photo() {}

    public Photo(String id, String url, String camera) {
        this.id = id;
        this.url = url;
        this.camera = camera;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }
}
