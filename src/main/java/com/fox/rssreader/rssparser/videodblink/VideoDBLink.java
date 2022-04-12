package com.fox.rssreader.rssparser.videodblink;

public abstract class VideoDBLink {
    private String id;
    private float rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract String getUrl();

    public abstract void setUrl(String url);

    public float getRating() {
        return rating;
    }

    public float getMaxRating() {
        return 10;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        return this.getId().equals(((VideoDBLink) obj).getId());
    }

}
