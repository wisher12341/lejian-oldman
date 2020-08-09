package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.PollLocationStatusRequest;
import com.lejian.oldman.controller.contract.response.GetLocationListResponse;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.service.LocationService;
import com.lejian.oldman.service.OldmanService;
import com.lejian.oldman.vo.LocationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/location")
public class LocationController {


    @Autowired
    private LocationService service;
    @Autowired
    private OldmanService oldmanService;

    @RequestMapping("/getAllLocation")
    public GetLocationListResponse getAllLocation(){
        GetLocationListResponse response = new GetLocationListResponse();
        response.setLocationVoList(service.getAllLocation());
        return response;
    }



    /**
     * 轮询楼最新状态
     */
    @RequestMapping("/pollStatus")
    public GetLocationListResponse pollStatus(@RequestBody PollLocationStatusRequest request){
        GetLocationListResponse response=new GetLocationListResponse();
        response.setServiceOldmanCount(oldmanService.getOldmanByStatus(OldmanEnum.Status.YELLOW));
        Pair<Long,List<LocationVo>> pair = service.pollStatus(request.getTimestamp());
        if(pair!=null) {
            response.setLocationVoList(pair.getSecond());
            response.setTimestamp(pair.getFirst());
        }
        return response;
    }

}
