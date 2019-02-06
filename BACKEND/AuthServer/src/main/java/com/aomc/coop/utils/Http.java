package com.aomc.coop.utils;

public class Http {

    private static String host = "http:localhost";

    private static String port = ":8082";

    private static String ip = host + port;

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Http.ip = ip;
    }


}
