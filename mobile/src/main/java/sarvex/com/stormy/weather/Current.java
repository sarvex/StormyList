package sarvex.com.stormy.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import sarvex.com.stormy.R.drawable;

public class Current {
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipitation;
    private String summary;
    private String timezone;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconId() {
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        int iconId = drawable.clear_day;

        if (icon.equals("clear-day")) {
            iconId = drawable.clear_day;
        } else if (icon.equals("clear-night")) {
            iconId = drawable.clear_night;
        } else if (icon.equals("rain")) {
            iconId = drawable.rain;
        } else if (icon.equals("snow")) {
            iconId = drawable.snow;
        } else if (icon.equals("sleet")) {
            iconId = drawable.sleet;
        } else if (icon.equals("wind")) {
            iconId = drawable.wind;
        } else if (icon.equals("fog")) {
            iconId = drawable.fog;
        } else if (icon.equals("cloudy")) {
            iconId = drawable.cloudy;
        } else if (icon.equals("partly-cloudy-day")) {
            iconId = drawable.partly_cloudy;
        } else if (icon.equals("partly-cloudy-night")) {
            iconId = drawable.cloudy_night;
        }

        return iconId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
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
}
