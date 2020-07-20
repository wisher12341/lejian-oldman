package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.GetWorkerByPageRequest;
import com.lejian.oldman.controller.contract.request.GetWorkerPositionByTimeAndIdRequest;
import com.lejian.oldman.controller.contract.request.PageParam;
import com.lejian.oldman.controller.contract.response.GetWorkerListResponse;
import com.lejian.oldman.controller.contract.request.WorkerCheckInRequest;
import com.lejian.oldman.controller.contract.response.GetWorkerResponse;
import com.lejian.oldman.controller.contract.response.WorkerCheckInResponse;
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

    /**
     * 分页获取服务人员
     */
    @RequestMapping("getWorkerByPage")
    public GetWorkerListResponse getWorkerByPage(@RequestBody GetWorkerByPageRequest request){
        GetWorkerListResponse response= new GetWorkerListResponse();
        response.setWorkerVoList(workerService.getWorkerByPage(request.getPageParam(),request.getStartTime(),request.getEndTime(),request.getLocation()));
        return response;
    }


    /**
     * 获取某个时间段  特定阿姨  全部位置信息
     */
    @RequestMapping("/getWorkerPositionByTime")
    public GetWorkerResponse getWorkerPositionByTime(@RequestBody GetWorkerPositionByTimeAndIdRequest request){
        GetWorkerResponse response=new GetWorkerResponse();
        response.setWorkerVo(workerService.getWorkerPositionByTime(request.getStartTime(),request.getEndTime(),request.getWorkerId()));
        return response;
    }

}
