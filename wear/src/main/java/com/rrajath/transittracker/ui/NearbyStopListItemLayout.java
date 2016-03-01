package com.rrajath.transittracker.ui;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrajath.transittracker.R;

public class NearbyStopListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {
    private TextView nearbyStopName;

    public NearbyStopListItemLayout(Context context) {
        super(context, null);
    }

    public NearbyStopListItemLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public NearbyStopListItemLayout(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nearbyStopName = (TextView) findViewById(R.id.stop_name);
    }

    @Override
    public void onCenterPosition(boolean b) {
        nearbyStopName.setAlpha(1f);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        nearbyStopName.setAlpha(0.6f);
    }
}
