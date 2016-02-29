package com.rrajath.transittracker.ui.adapter;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrajath.shared.model.WearStop;
import com.rrajath.transittracker.R;

import java.util.List;

public class NearbyStopsAdapter extends WearableListView.Adapter {

    Context mContext;
    List<WearStop> mWearStops;
    LayoutInflater mLayoutInflater;

    public NearbyStopsAdapter(Context context, List<WearStop> wearStops) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mWearStops = wearStops;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearStopViewHolder(mLayoutInflater.inflate(R.layout.stop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        WearStopViewHolder viewHolder = (WearStopViewHolder) holder;
        WearStop wearStop = mWearStops.get(position);
        viewHolder.direction.setText(wearStop.getDirection());
        viewHolder.stopName.setText(wearStop.getName());
    }

    @Override
    public int getItemCount() {
        return mWearStops.size();
    }

    public static class WearStopViewHolder extends WearableListView.ViewHolder {
        TextView direction;
        TextView stopName;

        public WearStopViewHolder(View view) {
            super(view);
            direction = (TextView) view.findViewById(R.id.direction);
            stopName = (TextView) view.findViewById(R.id.stop_name);
        }
    }

}
