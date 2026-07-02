package com.example.more.leisu.data;

import androidx.annotation.NonNull;

public class PostConfigData {
    public enum ConfigType {
        SingleBasketball,
        MultiBasketball,
        SingleFootball,
        MultiFootball
    }

    private ConfigType type;

    private String title;

    private Boolean isPost;

    private Boolean isFree;

    private int postTimes;

    public PostConfigData(ConfigType type, String title, Boolean isPost, Boolean isFree, int postTimes) {
        this.postTimes = postTimes;
        this.isFree = isFree;
        this.isPost = isPost;
        this.title = title;
        this.type = type;
    }

    public PostConfigData() {

    }


    public int getPostTimes() {
        return postTimes;
    }

    public void setPostTimes(int postTimes) {
        this.postTimes = postTimes;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean free) {
        isFree = free;
    }

    public Boolean getIsPost() {
        return isPost;
    }

    public void setIsPost(Boolean post) {
        isPost = post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ConfigType getType() {
        return type;
    }

    public void setType(ConfigType type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "type=" + type.toString() + "--->title=" + title + "--->isPost=" + isPost + "--->isFree=" + isFree + "--->postTimes" + postTimes + "\n";
    }
}
