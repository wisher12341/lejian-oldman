package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.vo.CareAlarmRecordVo;
import com.lejian.oldman.vo.LocationVo;
import lombok.Data;

import java.util.List;

@Data
public class PollResponse {
    /**
     * 状态变化的楼
     */
    private List<LocationVo> locationVoList;
    /**
     * 用于轮询接口， 数据的最新时间
     */
    private Long timestamp;


    private Long alarmCount;
    private Long yellowOldmanCount;
    private Long workerCheckInCount;
    private Long organServiceCount;
    private Long workerCount;
    private Long equipCount;
    private Long homeServiceCount;
    private Long rzzCount;
    private Long dbCount;


    /**
     * 新增的报警
     */
    private List<CareAlarmRecordVo> careAlarmRecordVoList;

    /**
     * 报警数据的 时间戳
     */
    private Long careTimestamp;
}
