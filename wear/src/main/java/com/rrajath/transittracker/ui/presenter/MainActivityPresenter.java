package com.rrajath.transittracker.ui.presenter;

import com.rrajath.transittracker.ui.activity.MainActivity;

public class MainActivityPresenter {
    MainActivity mMainActivity;

    public MainActivityPresenter(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void onNearbyMenuItemClick() {
        mMainActivity.sendMessageToHandheld("/nearby");
    }

    public void onStarredMenuItemClick() {
        mMainActivity.sendMessageToHandheld("/starred");
    }
}
