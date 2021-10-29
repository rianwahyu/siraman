package com.rigadev.siraman.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KCal {

    public String konvertdate (String a, String b, String c) {
        SimpleDateFormat sdf = new SimpleDateFormat(b);
        Date testDate = null;
        try {
            testDate = sdf.parse(a);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(c);
        String newFormat = formatter.format(testDate);
        return newFormat;
    }
}
