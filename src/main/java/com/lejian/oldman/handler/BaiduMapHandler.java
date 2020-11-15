package com.lejian.oldman.handler;

import com.lejian.oldman.utils.HttpUtils;
import com.lejian.oldman.utils.SerializationUtils;
import lombok.Data;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

/**
 * 百度地图
 *
 */
@Component
public class BaiduMapHandler {

    private static final String AK="6YSpoya23ILZkeQeyRGdt8s0CVzhKtzG";

    /**
     * 文档：http://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
     */
    private static final String ADDRESS_ANALYZER_URL_TEMPLATE=
            "http://api.map.baidu.com/geocoding/v3/?address=%s&ak=%s&city=%s&output=json";


    private static final int SUCCESS_STATUS=0;

    /**
     * 精确查找
     */
    private static final int RIGHT_PRECISE=1;

    private static final int CONFIDENCE_MIN=70;

    private static final int COMPREHENSION_MIN=80;




    /**
     *地理编码
     */
    //todo 加缓存
    public Pair<String,String> geocoding(String address,String city){
        String url = String.format(ADDRESS_ANALYZER_URL_TEMPLATE,address,AK,city);
        GeoCodingBo geoCodingBo= SerializationUtils.gsonSerialize(HttpUtils.get(url),GeoCodingBo.class);
        if(verifyGeoResult(geoCodingBo)){
            return Pair.of(String.valueOf(geoCodingBo.getResult().getLocation().getLng())
                    , String.valueOf(geoCodingBo.getResult().getLocation().getLat()));
        }
        return null;
    }

    /**
     * 验证 地理编码查询的结果是否符合要求
     * 1. 返回成功
     * 2. 为精确查找
     * 3. 精度 confidence>= 70 && comprehension>=80
     * @param geoCodingBo
     * @return
     */
    private boolean verifyGeoResult(GeoCodingBo geoCodingBo) {
        if(geoCodingBo.getStatus()!=SUCCESS_STATUS) {
            return false;
        }
        if(geoCodingBo.getResult().getPrecise()!=RIGHT_PRECISE){
            return false;
        }
        if(geoCodingBo.getResult().getConfidence()<CONFIDENCE_MIN
                || geoCodingBo.getResult().getComprehension()<COMPREHENSION_MIN ){
            return false;
        }
        return true;
    }


    /**
     * 百度地图返回的数据结构
     */
    @Data
    public class GeoCodingBo{
        private Integer status;
        private GeoResult result;

    }

    @Data
    class GeoResult {
        private GeoLocation location;
        private Integer precise;
        private Integer confidence;
        private Integer comprehension;
        private String level;
    }

    @Data
    class GeoLocation {
        private Float lat;
        private Float lng;
    }


}
