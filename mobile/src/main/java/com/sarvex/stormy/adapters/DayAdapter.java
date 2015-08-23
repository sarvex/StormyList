package com.sarvex.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarvex.stormy.R.id;
import com.sarvex.stormy.R.layout;
import com.sarvex.stormy.weather.Day;

public class DayAdapter extends BaseAdapter {

    private final Context context;
    private final Day[] days;

    public DayAdapter(Context context, Day[] days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; // not used: Tag items for easy use
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) view.findViewById(id.iconImageView);
            holder.temperatureLabel = (TextView) view.findViewById(id.temperatureLabel);
            holder.dayLabel = (TextView) view.findViewById(id.dayNameLabel);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Day day = days[position];
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(String.valueOf(day.getTemperatureMax()));

        if (position == 0) {
            holder.dayLabel.setText("Today");
        } else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }

        return view;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
