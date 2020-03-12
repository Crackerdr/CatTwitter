package com.crackerdr.catreddit;

public class CatCard {
    private String title;
    private String postURL;
    private String thumbnailURL;
    private String id;

    public CatCard(String title, String postURL, String thumbnailURL, String id) {
        this.title = title;
        this.postURL = postURL;
        this.thumbnailURL = thumbnailURL;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
