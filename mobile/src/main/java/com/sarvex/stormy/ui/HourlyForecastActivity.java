package com.sarvex.stormy.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

import com.sarvex.stormy.R;
import com.sarvex.stormy.R.layout;
import com.sarvex.stormy.adapters.HourAdapter;
import com.sarvex.stormy.utility.Intend;
import com.sarvex.stormy.weather.Hour;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HourlyForecastActivity extends AppCompatActivity {

  private List<Hour> hours;
  @Bind(R.id.recycler_view) RecyclerView recyclerView;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(layout.activity_hourly_forecast);
    ButterKnife.bind(this);

    hours = getIntent().getParcelableArrayListExtra(Intend.HOURLY_FORECAST);
    recyclerView.setAdapter(new HourAdapter(hours));
    final LayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
  }
}
