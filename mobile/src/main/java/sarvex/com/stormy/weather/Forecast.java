package sarvex.com.stormy.weather;

import sarvex.com.stormy.R.drawable;

public class Forecast {
    private Current current;
    private Hour[] hourlyForecast;
    private Day[] dailyForecast;

    public static int getIconId(String icon) {
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.
        int iconId = drawable.clear_day;

        switch (icon) {
            case "clear-day":
                iconId = drawable.clear_day;
                break;
            case "clear-night":
                iconId = drawable.clear_night;
                break;
            case "rain":
                iconId = drawable.rain;
                break;
            case "snow":
                iconId = drawable.snow;
                break;
            case "sleet":
                iconId = drawable.sleet;
                break;
            case "wind":
                iconId = drawable.wind;
                break;
            case "fog":
                iconId = drawable.fog;
                break;
            case "cloudy":
                iconId = drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = drawable.cloudy_night;
                break;
        }

        return iconId;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Hour[] getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return dailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        this.dailyForecast = dailyForecast;
    }


}
