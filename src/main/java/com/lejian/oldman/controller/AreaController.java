package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.GetAreaRequest;
import com.lejian.oldman.controller.contract.response.GetEnumResponse;
import com.lejian.oldman.controller.contract.response.MapResponse;
import com.lejian.oldman.enums.OldmanEnum;
import com.lejian.oldman.service.AreaService;
import com.lejian.oldman.service.EnumService;
import com.lejian.oldman.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 */

@Controller
@ResponseBody
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService service;

    @RequestMapping("/get")
    public MapResponse get(@RequestBody GetAreaRequest request){
        MapResponse response =new MapResponse();
        service.getByTypeAndParenId(request.getType(),request.getParentId());
        return response;
    }


}
