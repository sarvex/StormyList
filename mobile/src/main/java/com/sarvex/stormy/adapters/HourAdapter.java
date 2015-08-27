package com.sarvex.stormy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarvex.stormy.R;
import com.sarvex.stormy.weather.Hour;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sarvex Jatasra on 8/27/2015.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.ViewHolder> {

  private List<Hour> hours;

  public HourAdapter(List<Hour> hours) {
    this.hours = hours;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bindHour(hours.get(position));
  }

  @Override
  public int getItemCount() {
    return hours.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.time_label) TextView timeLabel;
    @Bind(R.id.summary_label) TextView summaryLabel;
    @Bind(R.id.temperature_label) TextView temperatureLabel;
    @Bind(R.id.icon_image_view) ImageView iconImageView;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bindHour(Hour hour) {
      timeLabel.setText(hour.getHour());
      summaryLabel.setText(hour.getSummary());
      temperatureLabel.setText(String.valueOf(hour.getTemperature()));
      iconImageView.setImageResource(hour.getIconId());
    }
  }
}
