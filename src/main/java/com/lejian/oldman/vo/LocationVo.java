package com.lejian.oldman.vo;

import com.lejian.oldman.enums.OldmanStatusEnum;
import com.lejian.oldman.exception.PendingException;
import lombok.Data;

@Data
public class LocationVo {

    private String desc;
    private String positionX;
    private String positionY;
    private LocationTypeEnum locationTypeEnum;



    public enum LocationTypeEnum{
        /**
         * 全是绿灯
         */
        GREEN {
            @Override
            public LocationTypeEnum convert(OldmanStatusEnum oldmanStatus) {
                return LocationTypeEnum.find(oldmanStatus);
        }
        },
        /**
         * 存在红灯的老人
         */
        RED {
            @Override
            public LocationTypeEnum convert(OldmanStatusEnum oldmanStatus) {
                LocationTypeEnum locationTypeEnum = LocationTypeEnum.find(oldmanStatus);
                if(locationTypeEnum == YELLOW){
                    return RANDY;
                }
                return this;
            }
        },
        /**
         * 存在黄灯的老人
         */
        YELLOW {
            @Override
            public LocationTypeEnum convert(OldmanStatusEnum oldmanStatus) {
                LocationTypeEnum locationTypeEnum = LocationTypeEnum.find(oldmanStatus);
                if(locationTypeEnum == RED){
                    return RANDY;
                }
                return this;
            }
        },
        /**
         * 同时存在黄灯和红灯的老人
         */
        RANDY {
            @Override
            public LocationTypeEnum convert(OldmanStatusEnum oldmanStatus) {
                return this;
            }
        };

        /**
         * 根据老人状态 转化 该位置的状态
         * @param oldmanStatus
         * @return
         */
        public abstract LocationTypeEnum convert(OldmanStatusEnum oldmanStatus);


        public static LocationTypeEnum find(OldmanStatusEnum oldmanStatus){
            for(LocationTypeEnum typeEnum:LocationTypeEnum.values()){
                if(typeEnum.name().equals(oldmanStatus.name())){
                    return typeEnum;
                }
            }
            throw new PendingException("no LocationTypeEnum found");
        }
    }


}
