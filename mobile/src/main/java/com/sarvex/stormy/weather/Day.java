package com.sarvex.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Day implements Parcelable {
  public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
    @Override
    public Day createFromParcel(Parcel parcel) {
      return new Day(parcel);
    }

    @Override
    public Day[] newArray(int size) {
      return new Day[size];
    }
  };

  private long time;
  private String summary;
  private double temperatureMax;
  private String icon;
  private String timezone;

  public Day() {
  }

  private Day(Parcel parcel) {
    time = parcel.readLong();
    summary = parcel.readString();
    temperatureMax = parcel.readDouble();
    icon = parcel.readString();
    timezone = parcel.readString();
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public int getTemperatureMax() {
    return (int) Math.round(temperatureMax);
  }

  public void setTemperatureMax(double temperatureMax) {
    this.temperatureMax = temperatureMax;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public int getIconId() {
    return Forecast.getIconId(icon);
  }

  public String getDayOfTheWeek() {
    SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
    formatter.setTimeZone(TimeZone.getTimeZone(timezone));
    Date dateTime = new Date(time * 1000);
    return formatter.format(dateTime);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeLong(time);
    parcel.writeString(summary);
    parcel.writeDouble(temperatureMax);
    parcel.writeString(icon);
    parcel.writeString(timezone);
  }
}
