package com.sarvex.stormy.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Current {
  private String icon;
  private long time;
  private double temperature;
  private double humidity;
  private double precipitation;
  private String summary;
  private String timezone;

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getFormattedTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
    formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
    Date dateTime = new Date(getTime() * 1000);
    String timeString = formatter.format(dateTime);

    return timeString;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public int getTemperature() {
    return (int) Math.round(temperature);
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getHumidity() {
    return humidity;
  }

  public void setHumidity(double humidity) {
    this.humidity = humidity;
  }

  public int getPrecipitation() {
    return (int) Math.round(precipitation * 100);
  }

  public void setPrecipitation(double precipitation) {
    this.precipitation = precipitation;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public int getIconId() {
    return Forecast.getIconId(icon);
  }
}
