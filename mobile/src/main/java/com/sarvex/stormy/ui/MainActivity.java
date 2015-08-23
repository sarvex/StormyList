package com.sarvex.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sarvex.stormy.R.id;
import com.sarvex.stormy.R.layout;
import com.sarvex.stormy.R.string;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    private static final String HOURLY_FORECAST = "HOURLY_FORECAST";

    @Bind(id.timeLabel)
    TextView timeLabel;

    @Bind(id.temperatureLabel)
    TextView temperatureLabel;

    @Bind(id.humidityValue)
    TextView humidityValue;

    @Bind(id.precipitationValue)
    TextView precipitationValue;

    @Bind(id.summaryLabel)
    TextView summaryLabel;

    @Bind(id.iconImageView)
    ImageView iconImageView;

    @Bind(id.refreshImageView)
    ImageView refreshImageView;

    @Bind(id.progressBar)
    ProgressBar progressBar;

    private Forecast forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.INVISIBLE);

        final double latitude = 37.8267;
        final double longitude = -122.423;

        refreshImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);

        Log.d(MainActivity.TAG, "Main UI code is running!");
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "27974c4bc33201748eaf542a6769c3b7";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Builder()
                    .url(forecastUrl)
                    .build();

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
                    }
                    catch (IOException e) {
                        Log.e(MainActivity.TAG, "Exception caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(MainActivity.TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, getString(string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void toggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
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

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }

        return hours;
    }

    private Day[] getDailyForecast(String jsonData) {
        return new Day[0];
    }

    @OnClick(id.dailyButton)
    public void startDailyActivity(View view) {
        startActivity(new Intent(this, DailyForecastActivity.class).putExtra(MainActivity.DAILY_FORECAST, forecast.getDailyForecast()));
    }

    @OnClick(id.hourlyButton)
    public void startHourlyActivity(View view) {
        startActivity(new Intent(this, HourlyForecastActivity.class).putExtra(HOURLY_FORECAST, forecast.getHourlyForecast()));
    }
}














