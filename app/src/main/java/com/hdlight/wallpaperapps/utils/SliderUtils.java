package com.hdlight.wallpaperapps.utils;

public class SliderUtils {
    int cat_id;
    String cat_name;
    String sliderImageUrl;
    String slider_name;
    int type;
    String url;
    String wall_name;

    public String getSliderImageUrl() {
        return this.sliderImageUrl;
    }

    public void setSliderImageUrl(String str) {
        this.sliderImageUrl = str;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getWallName() {
        return this.wall_name;
    }

    public void setWallName(String str) {
        this.wall_name = str;
    }

    public String getSliderName() {
        return this.slider_name;
    }

    public void setSliderName(String str) {
        this.slider_name = str;
    }

    public String getCatName() {
        return this.cat_name;
    }

    public void setCatName(String str) {
        this.cat_name = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getCatId() {
        return this.cat_id;
    }

    public void setCatId(int i) {
        this.cat_id = i;
    }
}
