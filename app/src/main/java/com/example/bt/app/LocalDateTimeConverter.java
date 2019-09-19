package com.example.bt.app;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Utility class to parse seconds into LocalDate/Time class and vice versa.
 */
public class LocalDateTimeConverter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalTime GetLocalTimeFromSeconds(long localTimeInSeconds){
        int hours = (int) (localTimeInSeconds / 3600);
        int minutes = (int) (localTimeInSeconds / 60 - (hours * 60));

        return LocalTime.of(hours, minutes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate GetLocalDateFromEpochSeconds(long dateInSeconds){
        return Instant.ofEpochSecond(dateInSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long GetLocalDateInEpochSecond(LocalDate dStr){
        return dStr.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long GetLocalTimeInSeconds(LocalTime time){
        return time.toSecondOfDay();
    }
}
