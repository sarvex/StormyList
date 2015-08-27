package com.sarvex.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sarvex.stormy.R;
import com.sarvex.stormy.utility.Intend;
import com.sarvex.stormy.weather.Current;
import com.sarvex.stormy.weather.Day;
import com.sarvex.stormy.weather.Forecast;
import com.sarvex.stormy.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  public static final double LATITUDE = 37.8267;
  public static final double LONGITUDE = -122.423;

  @Bind(R.id.timeLabel) TextView timeLabel;
  @Bind(R.id.temperature_label) TextView temperatureLabel;
  @Bind(R.id.humidityValue) TextView humidityValue;
  @Bind(R.id.precipitationValue) TextView precipitationValue;
  @Bind(R.id.summaryLabel) TextView summaryLabel;
  @Bind(R.id.icon_image_view) ImageView iconImageView;
  @Bind(R.id.refreshImageView) ImageView refreshImageView;
  @Bind(R.id.progressBar) ProgressBar progressBar;

  private Forecast forecast;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    progressBar.setVisibility(View.INVISIBLE);
    getForecast(LATITUDE, LONGITUDE);
  }

  @OnClick(R.id.refreshImageView)
  public void refreshImageClicked(final View view) {
    getForecast(LATITUDE, LONGITUDE);
  }

  private void getForecast(final double latitude, final double longitude) {
    final String apiKey = "27974c4bc33201748eaf542a6769c3b7";
    final String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
        "/" + latitude + "," + longitude;

    if (isNetworkAvailable()) {
      toggleRefresh();

      final OkHttpClient client = new OkHttpClient();
      final Request request = new Builder().url(forecastUrl).build();

      final Call call = client.newCall(request);
      call.enqueue(new Callback() {
        @Override
        public void onFailure(final Request request, final IOException e) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              toggleRefresh();
            }
          });
          alertUserAboutError();
        }

        @Override
        public void onResponse(final Response response) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              toggleRefresh();
            }
          });

          try {
            final String jsonData = response.body().string();
            if (response.isSuccessful()) {
              forecast = parseForecastDetails(jsonData);
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  updateDisplay();
                }
              });
            } else {
              alertUserAboutError();
            }
          } catch (final IOException e) {
          } catch (final JSONException e) {
          }
        }
      });
    } else {
      Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
    }
  }

  private boolean isNetworkAvailable() {
    final ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    final NetworkInfo networkInfo = manager.getActiveNetworkInfo();
    boolean isAvailable = false;
    if ((networkInfo != null) && networkInfo.isConnected()) {
      isAvailable = true;
    }

    return isAvailable;
  }

  private void toggleRefresh() {
    if (progressBar.getVisibility() == View.INVISIBLE) {
      progressBar.setVisibility(View.VISIBLE);
      refreshImageView.setVisibility(View.INVISIBLE);
    } else {
      progressBar.setVisibility(View.INVISIBLE);
      refreshImageView.setVisibility(View.VISIBLE);
    }
  }

  private void alertUserAboutError() {
    final AlertDialogFragment dialog = new AlertDialogFragment();
    dialog.show(getFragmentManager(), "error_dialog");
  }

  private Forecast parseForecastDetails(final String json) throws JSONException {

    final Forecast forecast = new Forecast();

    forecast.setCurrent(getCurrentDetails(json));
    forecast.setHourlyForecast(getHourlyForecast(json));
    forecast.setDailyForecast(getDailyForecast(json));

    return forecast;
  }

  private void updateDisplay() {
    final Current current = forecast.getCurrent();

    temperatureLabel.setText(current.getTemperature() + "");
    timeLabel.setText("At " + current.getFormattedTime() + " it will be");
    humidityValue.setText(current.getHumidity() + "");
    precipitationValue.setText(current.getPrecipitation() + "%");
    summaryLabel.setText(current.getSummary());

    final Drawable drawable = getResources().getDrawable(current.getIconId());
    iconImageView.setImageDrawable(drawable);
  }

  private Current getCurrentDetails(final String json) throws JSONException {
    final JSONObject forecast = new JSONObject(json);
    final String timezone = forecast.getString(com.sarvex.stormy.utility.Response.TIMEZONE);
    final JSONObject currently = forecast.getJSONObject(com.sarvex.stormy.utility.Response.CURRENTLY);

    final Current current = new Current();
    current.setHumidity(currently.getDouble(com.sarvex.stormy.utility.Response.HUMIDITY));
    current.setTime(currently.getLong(com.sarvex.stormy.utility.Response.TIME));
    current.setIcon(currently.getString(com.sarvex.stormy.utility.Response.ICON));
    current.setPrecipitation(currently.getDouble(com.sarvex.stormy.utility.Response.PRECIP_PROBABILITY));
    current.setSummary(currently.getString(com.sarvex.stormy.utility.Response.SUMMARY));
    current.setTemperature(currently.getDouble(com.sarvex.stormy.utility.Response.TEMPERATURE));
    current.setTimezone(timezone);

    return current;
  }

  private List<Hour> getHourlyForecast(final String jsonData) throws JSONException {
    final JSONObject forecast = new JSONObject(jsonData);
    final String timezone = forecast.getString(com.sarvex.stormy.utility.Response.TIMEZONE);
    final JSONObject hourly = forecast.getJSONObject(com.sarvex.stormy.utility.Response.HOURLY);
    final JSONArray data = hourly.getJSONArray(com.sarvex.stormy.utility.Response.DATA);

    final List<Hour> hours = new ArrayList<>(data.length());
    for (int i = 0; i < data.length(); i++) {
      final JSONObject jsonHour = data.getJSONObject(i);
      final Hour hour = new Hour();

      hour.setSummary(jsonHour.getString(com.sarvex.stormy.utility.Response.SUMMARY));
      hour.setTemperature(jsonHour.getDouble(com.sarvex.stormy.utility.Response.TEMPERATURE));
      hour.setIcon(jsonHour.getString(com.sarvex.stormy.utility.Response.ICON));
      hour.setTime(jsonHour.getLong(com.sarvex.stormy.utility.Response.TIME));
      hour.setTimezone(timezone);

      hours.add(hour);
    }

    return hours;
  }

  private List<Day> getDailyForecast(final String json) throws JSONException {
    final JSONObject forecast = new JSONObject(json);
    final String timezone = forecast.getString(com.sarvex.stormy.utility.Response.TIMEZONE);
    final JSONObject daily = forecast.getJSONObject(com.sarvex.stormy.utility.Response.DAILY);
    final JSONArray data = daily.getJSONArray(com.sarvex.stormy.utility.Response.DATA);

    final List<Day> days = new ArrayList<>(data.length());
    for (int i = 0; i < data.length(); i++) {
      final JSONObject jsonDay = data.getJSONObject(i);
      final Day day = new Day();

      day.setSummary(jsonDay.getString(com.sarvex.stormy.utility.Response.SUMMARY));
      day.setIcon(jsonDay.getString(com.sarvex.stormy.utility.Response.ICON));
      day.setTemperatureMax(jsonDay.getDouble(com.sarvex.stormy.utility.Response.TEMPERATURE_MAX));
      day.setTime(jsonDay.getLong(com.sarvex.stormy.utility.Response.TIME));
      day.setTimezone(timezone);

      days.add(day);
    }

    return days;
  }

  @OnClick(R.id.daily_button)
  public void startDailyActivity(final View view) {
    startActivity(new Intent(this, DailyForecastActivity.class)
        .putParcelableArrayListExtra(Intend.DAILY_FORECAST, (ArrayList<Day>) forecast.getDailyForecast()));
  }

  @OnClick(R.id.hourly_button)
  public void startHourlyActivity(final View view) {
    startActivity(new Intent(this, HourlyForecastActivity.class)
        .putParcelableArrayListExtra(Intend.HOURLY_FORECAST, (ArrayList<Hour>) forecast.getHourlyForecast()));
  }
}
