package com.lejian.oldman.controller;

import com.lejian.oldman.bo.UserBo;
import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.GetWorkerListResponse;
import com.lejian.oldman.controller.contract.response.GetWorkerResponse;
import com.lejian.oldman.controller.contract.response.ResultResponse;
import com.lejian.oldman.handler.ExcelHandler;
import com.lejian.oldman.repository.UserRepository;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.security.annotation.WorkerAuth;
import com.lejian.oldman.service.WorkerService;
import com.lejian.oldman.utils.CookieUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static com.lejian.oldman.common.ComponentRespCode.ACCOUNT_ERROR;

@Controller
@ResponseBody
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private ExcelHandler excelHandler;


    /**
     * 服务人员签到
     * @param request
     * @return
     */
    @WorkerAuth
    @RequestMapping("/checkin")
    public ResultResponse checkIn(@RequestBody WorkerCheckInRequest request) {
        workerService.checkIn(request.getLng(),request.getLat(),request.getOid());
        return new ResultResponse();
    }


    /**
     * 分页获取服务人员
     */
    @BackUserAuth
    @RequestMapping("getWorkerByPage")
    public GetWorkerListResponse getWorkerByPage(@RequestBody GetWorkerByPageRequest request){
        GetWorkerListResponse response= new GetWorkerListResponse();
        response.setWorkerVoList(workerService.getWorkerByPage(request.getPageParam(),request.getWorkerSearchParam()));
        if(request.getNeedCount()) {
            response.setCount(workerService.getWorkerCount(request.getWorkerSearchParam()));
        }
        return response;
    }

    /**
     * 分页获取 该行政单位所属的服务人员某个时间段的地理位置
     */
    @BackUserAuth
    @RequestMapping("getWorkerPositionByPageAndArea")
    public GetWorkerListResponse getWorkerPositionByPage(@RequestBody GetWorkerPositionByPageRequest request){
        GetWorkerListResponse response= new GetWorkerListResponse();
        response.setWorkerVoList(workerService.getWorkerPositionByPage(request.getPageParam(),request.getStartTime(),request.getEndTime()));
        return response;
    }


    /**
     * 获取某个时间段  特定阿姨  全部位置信息
     */
    @BackAdminAuth
    @RequestMapping("/getWorkerPositionByTime")
    public GetWorkerResponse getWorkerPositionByTime(@RequestBody GetWorkerPositionByTimeAndIdRequest request){
        GetWorkerResponse response=new GetWorkerResponse();
        response.setWorkerVo(workerService.getWorkerPositionByTime(request.getStartTime(),request.getEndTime(),request.getWorkerId()));
        return response;
    }


    @BackUserAuth
    @RequestMapping("/getWorkerByWid")
    public GetWorkerResponse getWorkerByWid(@RequestBody GetWorkerByWidRequest request){
        GetWorkerResponse response = new GetWorkerResponse();
        response.setWorkerVo(workerService.getWorkerByWid(request.getWorkerId()));
        return response;
    }


    /**
     * excel导入
     * @param file
     * @return
     */
    @BackUserAuth
    //todo 限制  数量限制 一次1000？ 参数限制
    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public ModelAndView importExcel(@RequestParam MultipartFile file) {
        Pair<List<String>,List<List<String>>> excelData=excelHandler.parse(file,2);
        if(CollectionUtils.isNotEmpty(excelData.getSecond())) {
            workerService.addWorkerByExcel(excelData);
        }
        ModelAndView mv=new ModelAndView("/worker");
        return mv;
    }

    /**
     * 编辑
     * @return
     */
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveWorkerRequest request){
        ResultResponse response=new ResultResponse();
        workerService.editWorker(request.getWorkerParam());
        return response;
    }

    /**
     * 删除
     * @return
     */
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/delete")
    public ResultResponse delete(@RequestBody DeleteWorkerByIdRequest request){
        ResultResponse response=new ResultResponse();
        workerService.deleteWorker(request.getId());
        return response;
    }
}
