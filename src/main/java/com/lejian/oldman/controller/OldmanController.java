package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.*;
import com.lejian.oldman.service.OldmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/oldman")
public class OldmanController {


    @Autowired
    private OldmanService oldmanService;

    /**
     * 老人列表分页查询
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByPage")
    public GetOldmanByPageResponse getOldmanByPage(@RequestBody GetOldmanByPageRequest request){
        GetOldmanByPageResponse response = new GetOldmanByPageResponse();
        PageParam pageParam = request.getPageParam();
        response.setOldmanVoList(oldmanService.getOldmanByPage(pageParam.getPageNo(),pageParam.getPageSize(),request.getOldmanSearchParam()));
        response.setCount(oldmanService.getOldmanCount(request.getOldmanSearchParam()));
        return response;
    }


    /**
     * 根据locationid 获取全都老人
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByLocationId")
    public GetOldmanByLocationIdResponse getOldmanByLocationId(@RequestBody GetOldmanByLocationIdRequest request){
        GetOldmanByLocationIdResponse response = new GetOldmanByLocationIdResponse();
        response.setOldmanVoList(oldmanService.getOldmanByLocationId(request.getLocationId()));
        return response;
    }

    /**
     * oid
     * @param request
     * @return
     */
    @RequestMapping("/getOldmanByOid")
    public GetOldmanByOidResponse getOldmanByOid(@RequestBody GetOldmanByOidRequest request){
        GetOldmanByOidResponse response = new GetOldmanByOidResponse();
        response.setOldmanVo(oldmanService.getOldmanByOid(request.getOid()));
        return response;
    }
}
