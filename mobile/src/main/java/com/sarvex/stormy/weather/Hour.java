package com.sarvex.stormy.weather;

import android.os.Parcel;
import android.os.Parcelable;

@org.parceler.Parcel
public class Hour implements Parcelable {
  private long time;
  private String summary;
  private double temperature;
  private String icon;
  private String timezone;

  public Hour() {
  }

  private Hour(Parcel parcel) {
    time = parcel.readLong();
    summary = parcel.readString();
    temperature = parcel.readDouble();
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

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeLong(time);
    parcel.writeString(summary);
    parcel.writeDouble(temperature);
    parcel.writeString(icon);
    parcel.writeString(timezone);
  }
}
