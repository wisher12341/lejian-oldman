package com.lejian.oldman.enums;

import com.lejian.oldman.exception.PendingException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 老人颜色状态
 */

@Getter
@AllArgsConstructor
public enum  OldmanStatusEnum {

    GREEN(1,"正常"),
    YELLOW(2,"服务中"),
    RED(3,"异常");

    private Integer status;
    private String desc;

    public static OldmanStatusEnum find(Integer status){
        for(OldmanStatusEnum statusEnum: OldmanStatusEnum.values()){
            if(statusEnum.getStatus().intValue()==status){
                return statusEnum;
            }
        }
        throw new PendingException("no OldmanStatusEnum found");
    }
}
