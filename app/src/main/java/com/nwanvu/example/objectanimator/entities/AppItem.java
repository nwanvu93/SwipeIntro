package com.nwanvu.example.objectanimator.entities;

public class AppItem {
    private String appName = "";
    private String appId = "";
    private String appIcon = "";
    private double appRating = 0.0f;
    private String appAuthor = "";

    public AppItem() {
    }

    public AppItem(String appName, String appId, String appIcon, double appRating, String appAuthor) {
        this.appName = appName;
        this.appId = appId;
        this.appIcon = appIcon;
        this.appRating = appRating;
        this.appAuthor = appAuthor;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public double getAppRating() {
        return appRating;
    }

    public void setAppRating(double appRating) {
        this.appRating = appRating;
    }

    public String getAppAuthor() {
        return appAuthor;
    }

    public void setAppAuthor(String appAuthor) {
        this.appAuthor = appAuthor;
    }
}
