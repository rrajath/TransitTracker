package com.rrajath.transittracker.ui;

public class MainMenuItem {
    private String title;
    private int iconResource;

    public MainMenuItem(String title, int iconResource) {
        this.title = title;
        this.iconResource = iconResource;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResource() {
        return iconResource;
    }
}
