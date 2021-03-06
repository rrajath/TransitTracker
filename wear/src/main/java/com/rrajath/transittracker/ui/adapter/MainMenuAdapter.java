package com.rrajath.transittracker.ui.adapter;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrajath.transittracker.R;
import com.rrajath.transittracker.ui.MainMenuItem;

import java.util.List;

public class MainMenuAdapter extends WearableListView.Adapter {

    Context context;
    List<MainMenuItem> mainMenuItems;
    LayoutInflater mLayoutInflater;

    public MainMenuAdapter(Context context, List<MainMenuItem> mainMenuItems) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mainMenuItems = mainMenuItems;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuItemViewHolder(mLayoutInflater.inflate(R.layout.main_menu_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        MenuItemViewHolder viewHolder = (MenuItemViewHolder) holder;
        MainMenuItem mainMenuItem = mainMenuItems.get(position);
        viewHolder.menuName.setText(mainMenuItem.getTitle());
        viewHolder.menuName.setCompoundDrawablesWithIntrinsicBounds(mainMenuItem.getIconResource(), 0, 0, 0);
        viewHolder.menuName.setCompoundDrawablePadding(15);
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mainMenuItems.size();
    }

    public static class MenuItemViewHolder extends WearableListView.ViewHolder {

        TextView menuName;

        public MenuItemViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.main_menu_item_name);
        }
    }
}
