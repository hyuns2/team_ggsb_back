package com.example.ggsb_back.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public Date StringToDate(String date) throws ParseException {

        // 포맷터
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");

        // 문자열 -> Date
        return formatter.parse(date);
    }

}
