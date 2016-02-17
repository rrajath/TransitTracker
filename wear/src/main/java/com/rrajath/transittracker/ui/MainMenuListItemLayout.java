package com.rrajath.transittracker.ui;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrajath.transittracker.R;

public class MainMenuListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private TextView menuName;

    public MainMenuListItemLayout(Context context) {
        super(context, null);
    }

    public MainMenuListItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MainMenuListItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuName = (TextView) findViewById(R.id.main_menu_item_name);
    }

    @Override
    public void onCenterPosition(boolean b) {
        menuName.setAlpha(1f);
//        menuName.animate().scaleX(1.5f).scaleY(1.5f).alpha(1f);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
//        menuName.animate().scaleX(1f).scaleY(1f).alpha(0.6f);
        menuName.setAlpha(0.6f);
    }
}
