package com.aomc.coop.utils.date;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatCustom {

    public String sendDateFormat(Date date){
        SimpleDateFormat sendDateFormat = new SimpleDateFormat("EEEE, MMMM", Locale.US);
        String sendDate = sendDateFormat.format(date);

        SimpleDateFormat dayOfsendDateFormat = new SimpleDateFormat("d", Locale.US);
        String dayOfsendDate = dayOfsendDateFormat.format(date);

        String[] suffixes =
                        //    0     1     2     3     4     5     6     7     8     9
                        { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    10    11    12    13    14    15    16    17    18    19
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                        //    20    21    22    23    24    25    26    27    28    29
                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    30    31
                        "th", "st" };

        int day = Integer.parseInt(String.format(dayOfsendDate, date));
        String dayOfsendDateStr = day + suffixes[day];

        return sendDate+" "+dayOfsendDateStr;
    }

    public String sendTimeFormat(Date date){
        SimpleDateFormat sendTimeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        String sendTime = sendTimeFormat.format(date);

        return sendTime;
    }

    public String sendDBDateFormat(Date date){
        SimpleDateFormat sendDBDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String sendDBDate = sendDBDateFormat.format(date);

        return sendDBDate;
    }


}
