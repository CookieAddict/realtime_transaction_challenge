package com.transaction.service.utility;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Helper {
    
    @Value("${date.pattern}")
    private String datePattern;

    public String getFormattedSystemTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

        return simpleDateFormat.format(new Date());
    }
}
