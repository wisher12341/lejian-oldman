package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.GetOldmanByPageRequest;
import com.lejian.oldman.controller.contract.GetOldmanByPageResponse;
import com.lejian.oldman.controller.contract.PageParam;
import com.lejian.oldman.service.OldmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/oldman")
public class OldmanController {


    @Autowired
    private OldmanService oldmanService;

    @ResponseBody
    @RequestMapping("/getOldmanByPage")
    public GetOldmanByPageResponse getOldmanByPage(@RequestBody GetOldmanByPageRequest request){
        GetOldmanByPageResponse response = new GetOldmanByPageResponse();
        PageParam pageParam = request.getPageParam();
        response.setOldmanVoList(oldmanService.getOldmanByPage(pageParam.getPageNo(),pageParam.getPageSize(),request.getOldmanSearchParam()));
        response.setCount(oldmanService.getOldmanCount(request.getOldmanSearchParam()));
        return response;
    }
}
