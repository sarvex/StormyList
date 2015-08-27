package com.sarvex.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarvex.stormy.R;
import com.sarvex.stormy.weather.Day;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DayAdapter extends BaseAdapter {

  private final Context context;
  private final List<Day> days;

  public DayAdapter(final Context context, final List<Day> days) {
    this.context = context;
    this.days = days;
  }

  @Override
  public int getCount() {
    return days.size();
  }

  @Override
  public Object getItem(final int position) {
    return days.get(position);
  }

  @Override
  public long getItemId(final int position) {
    return 0;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    View view = convertView;
    final ViewHolder holder;

    if (view == null) {
      view = LayoutInflater.from(context).inflate(R.layout.daily_list_item, parent, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }

    final Day day = days.get(position);
    holder.iconImageView.setImageResource(day.getIconId());
    holder.temperatureLabel.setText(String.valueOf(day.getTemperatureMax()));

    if (position == 0) {
      holder.dayLabel.setText("Today");
    } else {
      holder.dayLabel.setText(day.getDayOfTheWeek());
    }

    return view;
  }

  static class ViewHolder {
    @Bind(R.id.icon_image_view) ImageView iconImageView;
    @Bind(R.id.temperature_label) TextView temperatureLabel;
    @Bind(R.id.day_name_label) TextView dayLabel;

    ViewHolder(final View view) {
      ButterKnife.bind(this, view);
    }
  }
}
