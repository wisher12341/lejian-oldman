package com.lejian.oldman.handler;

import com.lejian.oldman.utils.HttpUtils;
import com.lejian.oldman.utils.SerializationUtils;
import lombok.Data;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

/**
 * 百度地图
 */
@Component
public class BaiduMapHandler {

    private static final String AK="6YSpoya23ILZkeQeyRGdt8s0CVzhKtzG";

    private static final String ADDRESS_ANALYZER_URL_TEMPLATE=
            "http://api.map.baidu.com/geocoding/v3/?address=%s&ak=%s&city=%s&output=json";


    private static final int SUCCESS_STATUS=0;

    /**
     *地理编码
     */
    public Pair<String,String> geocoding(String address,String city){
        String url = String.format(ADDRESS_ANALYZER_URL_TEMPLATE,address,AK,city);
        GeoCodingBo geoCodingBo= SerializationUtils.gsonSerialize(HttpUtils.get(url),GeoCodingBo.class);
        if(geoCodingBo.getStatus()==SUCCESS_STATUS){
            return Pair.of(String.valueOf(geoCodingBo.getResult().getLocation().getLng())
                    , String.valueOf(geoCodingBo.getResult().getLocation().getLat()));
        }
        return null;
    }


    @Data
    public class GeoCodingBo{
        private Integer status;
        private GeoResult result;

    }

    @Data
    class GeoResult {
        private GeoLocation location;
    }

    @Data
    class GeoLocation {
        private Float lat;
        private Float lng;
    }


}
