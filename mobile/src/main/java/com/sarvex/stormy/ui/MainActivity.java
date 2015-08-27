package com.sarvex.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

  public static final String TAG = MainActivity.class.getSimpleName();
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
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    progressBar.setVisibility(View.INVISIBLE);
    getForecast(LATITUDE, LONGITUDE);
  }

  @OnClick(R.id.refreshImageView)
  public void refreshImageClicked(View view) {
    getForecast(LATITUDE, LONGITUDE);
  }

  private void getForecast(double latitude, double longitude) {
    String apiKey = "27974c4bc33201748eaf542a6769c3b7";
    String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
        "/" + latitude + "," + longitude;

    if (isNetworkAvailable()) {
      toggleRefresh();

      OkHttpClient client = new OkHttpClient();
      Request request = new Builder().url(forecastUrl).build();

      Call call = client.newCall(request);
      call.enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              toggleRefresh();
            }
          });
          alertUserAboutError();
        }

        @Override
        public void onResponse(Response response) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              toggleRefresh();
            }
          });

          try {
            String jsonData = response.body().string();
            Log.v(MainActivity.TAG, jsonData);
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
          } catch (IOException e) {
            Log.e(MainActivity.TAG, "Exception caught: ", e);
          } catch (JSONException e) {
            Log.e(MainActivity.TAG, "Exception caught: ", e);
          }
        }
      });
    } else {
      Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
    }
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
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
    AlertDialogFragment dialog = new AlertDialogFragment();
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

    Drawable drawable = getResources().getDrawable(current.getIconId());
    iconImageView.setImageDrawable(drawable);
  }

  private Current getCurrentDetails(String json) throws JSONException {
    JSONObject forecast = new JSONObject(json);
    String timezone = forecast.getString("timezone");
    Log.i(MainActivity.TAG, "From JSON: " + timezone);

    JSONObject currently = forecast.getJSONObject("currently");

    Current current = new Current();
    current.setHumidity(currently.getDouble("humidity"));
    current.setTime(currently.getLong("time"));
    current.setIcon(currently.getString("icon"));
    current.setPrecipitation(currently.getDouble("precipProbability"));
    current.setSummary(currently.getString("summary"));
    current.setTemperature(currently.getDouble("temperature"));
    current.setTimezone(timezone);

    Log.d(MainActivity.TAG, current.getFormattedTime());

    return current;
  }

  private List<Hour> getHourlyForecast(String jsonData) throws JSONException {
    JSONObject forecast = new JSONObject(jsonData);
    String timezone = forecast.getString("timezone");
    JSONObject hourly = forecast.getJSONObject("hourly");
    JSONArray data = hourly.getJSONArray("data");

    List<Hour> hours = new ArrayList<>(data.length());
    for (int i = 0; i < data.length(); i++) {
      JSONObject jsonHour = data.getJSONObject(i);
      Hour hour = new Hour();

      hour.setSummary(jsonHour.getString("summary"));
      hour.setTemperature(jsonHour.getDouble("temperature"));
      hour.setIcon(jsonHour.getString("icon"));
      hour.setTime(jsonHour.getLong("time"));
      hour.setTimezone(timezone);

      hours.add(hour);
    }

    return hours;
  }

  private List<Day> getDailyForecast(String json) throws JSONException {
    JSONObject forecast = new JSONObject(json);
    String timezone = forecast.getString("timezone");
    JSONObject daily = forecast.getJSONObject("daily");
    JSONArray data = daily.getJSONArray("data");

    List<Day> days = new ArrayList<>(data.length());
    for (int i = 0; i < data.length(); i++) {
      JSONObject jsonDay = data.getJSONObject(i);
      Day day = new Day();

      day.setSummary(jsonDay.getString("summary"));
      day.setIcon(jsonDay.getString("icon"));
      day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
      day.setTime(jsonDay.getLong("time"));
      day.setTimezone(timezone);

      days.add(day);
    }

    return days;
  }

  @OnClick(R.id.daily_button)
  public void startDailyActivity(View view) {
    startActivity(new Intent(this, DailyForecastActivity.class)
        .putParcelableArrayListExtra(Intend.DAILY_FORECAST, (ArrayList<Day>) forecast.getDailyForecast()));
  }

  @OnClick(R.id.hourly_button)
  public void startHourlyActivity(View view) {
    startActivity(new Intent(this, HourlyForecastActivity.class)
        .putParcelableArrayListExtra(Intend.HOURLY_FORECAST, (ArrayList<Hour>) forecast.getHourlyForecast()));
  }
}
