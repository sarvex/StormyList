package com.sarvex.stormy.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.sarvex.stormy.R.layout;
import com.sarvex.stormy.adapters.DayAdapter;
import com.sarvex.stormy.utility.Intend;
import com.sarvex.stormy.weather.Day;

import java.util.Arrays;
import java.util.List;


public class DailyForecastActivity extends ListActivity {

  private List<Day> days;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_daily_forecast);

    days = getIntent().getParcelableArrayListExtra(Intend.DAILY_FORECAST);

    DayAdapter dayAdapter = new DayAdapter(this, days);

    List<String> daysOfTheWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daysOfTheWeek);
    setListAdapter(arrayAdapter);
  }
}
