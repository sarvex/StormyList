package com.sarvex.stormy.ui;

import android.app.ListActivity;
import android.os.Bundle;

import com.sarvex.stormy.R.layout;
import com.sarvex.stormy.adapters.DayAdapter;
import com.sarvex.stormy.utility.Intend;
import com.sarvex.stormy.weather.Day;

import java.util.List;


public class DailyForecastActivity extends ListActivity {

  private List<Day> days;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_daily_forecast);

    days = getIntent().getParcelableArrayListExtra(Intend.DAILY_FORECAST);
    DayAdapter adapter = new DayAdapter(this, days);
    setListAdapter(adapter);

  }
}
