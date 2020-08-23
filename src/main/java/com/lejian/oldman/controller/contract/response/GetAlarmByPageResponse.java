package com.lejian.oldman.controller.contract.response;

import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.vo.CareAlarmRecordVo;
import lombok.Data;

import java.util.List;

@Data
public class GetAlarmByPageResponse {
    private List<CareAlarmRecordVo> alarmVoList;
    private Long count;
}
