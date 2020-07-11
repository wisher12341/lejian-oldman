package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.WorkerCheckInRequest;
import com.lejian.oldman.controller.contract.WorkerCheckInResponse;
import com.lejian.oldman.service.WorkerService;
import com.lejian.oldman.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    /**
     * 服务人员签到
     * @param request
     * @return
     */
    @RequestMapping("/checkin")
    public WorkerCheckInResponse checkIn(@RequestBody WorkerCheckInRequest request,HttpServletRequest httpServletRequest){
        String token = CookieUtils.getCookie(httpServletRequest,"token");
        workerService.checkIn(request.getLng(),request.getLat(),request.getOid(),token);
        return new WorkerCheckInResponse();
    }
}
