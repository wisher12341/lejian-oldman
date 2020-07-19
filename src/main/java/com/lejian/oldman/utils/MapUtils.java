package com.lejian.oldman.utils;

/**
 * 百度地图
 */
public final class MapUtils {

    private static final double EARTH_RADIUS = 6371393; // 地球平均半径,单位：m

    /**
     * 计算两点间距离（百度地图） 单位米
     * @return
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     */
    public static double distance(String longitude1, String latitude1, String longitude2, String latitude2){
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(Double.parseDouble(longitude1)); // A经弧度
        double radiansAY = Math.toRadians(Double.parseDouble(latitude1)); // A纬弧度
        double radiansBX = Math.toRadians(Double.parseDouble(longitude2)); // B经弧度
        double radiansBY = Math.toRadians(Double.parseDouble(latitude2)); // B纬弧度

        // cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
        double acos = Math.acos(cos); // 反余弦值
        return EARTH_RADIUS * acos; // 最终结果
    }
}
