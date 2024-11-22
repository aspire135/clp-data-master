package com.yzu.clp.Util;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {

    public static Timestamp getUtcPlus8Timestamp() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC+8"));
        return Timestamp.from(zonedDateTime.toInstant());
    }

}
