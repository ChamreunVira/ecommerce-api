package com.kh.vira_dev.ecommerceapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String generateOrderCode() {
        return "ORD-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    public static String generateTrackingNumber(String country, String city) {
        return country.toUpperCase().substring(0, 3) + "-" + city.toUpperCase().substring(0, 3) + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String generateOtpCode() {
        int opt = ThreadLocalRandom.current().nextInt(100000 , 1000000);
        return String.valueOf(opt);
    }

}
