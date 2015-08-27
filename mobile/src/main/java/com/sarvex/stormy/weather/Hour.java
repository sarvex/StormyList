package com.sarvex.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@org.parceler.Parcel
public class Hour implements Parcelable {
  public static final Creator<Hour> CREATOR = new Creator<Hour>() {
    @Override
    public Hour createFromParcel(final Parcel in) {
      return new Hour(in);
    }

    @Override
    public Hour[] newArray(final int size) {
      return new Hour[size];
    }
  };

  private long time;
  private String summary;
  private double temperature;
  private String icon;
  private String timezone;

  public Hour() {
  }

  private Hour(final Parcel parcel) {
    time = parcel.readLong();
    summary = parcel.readString();
    temperature = parcel.readDouble();
    icon = parcel.readString();
    timezone = parcel.readString();
  }

  public long getTime() {
    return time;
  }

  public void setTime(final long time) {
    this.time = time;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(final String summary) {
    this.summary = summary;
  }

  public int getTemperature() {
    return (int) Math.round(temperature);
  }

  public void setTemperature(final double temperature) {
    this.temperature = temperature;
  }

  public String getIcon() {
    return icon;
  }

  public int getIconId() {
    return Forecast.getIconId(icon);
  }

  public void setIcon(final String icon) {
    this.icon = icon;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(final String timezone) {
    this.timezone = timezone;
  }

  public String getHour() {
    SimpleDateFormat formatter = new SimpleDateFormat("h a");
    formatter.setTimeZone(TimeZone.getTimeZone(timezone));
    Date date = new Date(time * 1000);
    return formatter.format(date);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(final Parcel parcel, final int flags) {
    parcel.writeLong(time);
    parcel.writeString(summary);
    parcel.writeDouble(temperature);
    parcel.writeString(icon);
    parcel.writeString(timezone);
  }
}
