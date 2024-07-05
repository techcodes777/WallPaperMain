package com.hdlight.wallpaperapps.model;

public class Like {

    private String image;
    private int position;

    public Like(String image,int position) {
        this.image = image;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
