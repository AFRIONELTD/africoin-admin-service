package com.afrione.africoinservice.utils;

import com.afrione.africoinservice.domain.entities.enums.ClientTypeConstant;
import com.afrione.africoinservice.usecases.data.value_objects.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class WebUtils {
    public static TimeZone getTimeZone(HttpServletRequest servletRequest) {
        String timeZoneName = servletRequest.getHeader("x-timezone");
        if(StringUtils.isNotEmpty(timeZoneName)) {
            return TimeZone.getTimeZone(timeZoneName);
        }
        TimeZone timezone = RequestContextUtils.getTimeZone(servletRequest);
        return timezone == null ? TimeZone.getTimeZone("Africa/Lagos") : timezone;
    }

    public static String extractClientRequestIP(HttpServletRequest servletRequest) {
        String ipAddress = servletRequest.getHeader("X-FORWARDED-FOR");
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress =  servletRequest.getHeader("RemoteAddr");
            if (StringUtils.isEmpty(ipAddress)) {
                ipAddress = servletRequest.getHeader("x-real-ip");
            }
        }
        ipAddress = ipAddress != null && ipAddress.contains(",") ? ipAddress.split(",")[0] : ipAddress;
        return ipAddress;
    }

    public static ClientTypeConstant getClientType(HttpServletRequest servletRequest) {
        String clientType = (String)servletRequest.getAttribute(AppConstant.CLIENT_TYPE);
        System.out.println("clientType: " + clientType);
        if(StringUtils.isNotEmpty(clientType)) {
            return ClientTypeConstant.valueOf(clientType);
        }
        return ClientTypeConstant.WEB;
    }

    public static OffsetDateTime getOffSetTime(LocalDate localDate, TimeZone timeZone, boolean startTime) {
        ZoneId zoneId = timeZone != null ? timeZone.toZoneId(): ZoneId.of("Africa/Lagos");
        return startTime ? localDate.atStartOfDay(zoneId).toOffsetDateTime() : localDate.atTime(LocalTime.MAX).atZone(zoneId).toOffsetDateTime();
    }

}
