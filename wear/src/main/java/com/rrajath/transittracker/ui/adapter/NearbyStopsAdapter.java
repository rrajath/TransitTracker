package com.rrajath.transittracker.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrajath.shared.model.WearStop;
import com.rrajath.transittracker.R;

import java.util.List;

public class NearbyStopsAdapter extends RecyclerView.Adapter<NearbyStopsAdapter.WearStopViewHolder> {

    List<WearStop> mWearStops;

    public NearbyStopsAdapter(List<WearStop> wearStops) {
        mWearStops = wearStops;
    }

    @Override
    public WearStopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_item, parent, false);
        return new WearStopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WearStopViewHolder holder, int position) {
        WearStop wearStop = mWearStops.get(position);
        holder.direction.setText(wearStop.getDirection());
        holder.stopName.setText(wearStop.getName());
    }

    @Override
    public int getItemCount() {
        return mWearStops.size();
    }

    static class WearStopViewHolder extends RecyclerView.ViewHolder {
        TextView direction;
        TextView stopName;

        public WearStopViewHolder(View view) {
            super(view);
            direction = (TextView) view.findViewById(R.id.direction);
            stopName = (TextView) view.findViewById(R.id.stop_name);
        }
    }

}
