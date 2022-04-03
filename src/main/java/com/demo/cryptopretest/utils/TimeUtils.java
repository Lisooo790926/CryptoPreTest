package com.demo.cryptopretest.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class TimeUtils {

    private static Map<Character, Integer> timeUnitMap = Map.of(
            'm', Calendar.MINUTE, 'h', Calendar.HOUR,
            'D', Calendar.DAY_OF_MONTH, 'M', Calendar.MONTH);

    public static Date getLastTimeSlot(String timeframe, Date currentTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);

        char timeUnit = timeframe.charAt(timeframe.length() - 1);

        if (!timeUnitMap.containsKey(timeUnit)) {
            throw new IllegalArgumentException("Timeframe unit is wrong");
        }

        Integer iTimeUnit = timeUnitMap.get(timeUnit);
        calendar.set(iTimeUnit, calendar.get(iTimeUnit) - getTimeNumber(timeframe));

        return calendar.getTime();
    }

    private static int getTimeNumber(String timeframe) {

        String[] times = timeframe.split("[mhDM]");

        if (times.length != 1) {
            throw new IllegalArgumentException("Timeframe format is wrong");
        }
        return Integer.parseInt(times[0]);
    }

}
