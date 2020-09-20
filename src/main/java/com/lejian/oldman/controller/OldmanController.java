package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.*;
import com.lejian.oldman.handler.ExcelHandler;
import com.lejian.oldman.service.OldmanService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/oldman")
public class OldmanController {


    @Autowired
    private OldmanService oldmanService;
    @Autowired
    private ExcelHandler excelHandler;

    /**
     * 老人列表分页查询
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOldmanByPage")
    public GetOldmanByPageResponse getOldmanByPage(@RequestBody GetOldmanByPageRequest request){
        GetOldmanByPageResponse response = new GetOldmanByPageResponse();
        PageParam pageParam = request.getPageParam();
        response.setOldmanVoList(oldmanService.getOldmanByPage(pageParam.getPageNo(),pageParam.getPageSize(),request.getOldmanSearchParam()));
        if(request.getNeedCount()) {
            response.setCount(oldmanService.getOldmanCount(request.getOldmanSearchParam()));
        }
        return response;
    }


    /**
     * 根据locationid 获取全都老人
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOldmanByLocationId")
    public GetOldmanListResponse getOldmanByLocationId(@RequestBody GetOldmanByLocationIdRequest request){
        GetOldmanListResponse response = new GetOldmanListResponse();
        response.setOldmanVoList(oldmanService.getOldmanByLocationId(request.getLocationId()));
        return response;
    }

    /**
     * oid
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOldmanByOid")
    public GetOldmanResponse getOldmanByOid(@RequestBody GetOldmanByOidRequest request){
        GetOldmanResponse response = new GetOldmanResponse();
        response.setOldmanVo(oldmanService.getOldmanByOid(request.getOid()));
        return response;
    }

    /**
     * 模糊姓名查询
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOldmanByFuzzyName")
    public GetOldmanListResponse getOldmanByFuzzyName(@RequestBody GetOldmanByFuzzNameRequest request){
        GetOldmanListResponse response = new GetOldmanListResponse();
        response.setOldmanVoList(oldmanService.getOldmanByFuzzyName(request.getValue()));
        return response;
    }

    /**
     * 根据姓名查询
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOldmanByName")
    public GetOldmanResponse getOldmanByName(@RequestBody GetOldmanByNameRequest request){
        GetOldmanResponse response = new GetOldmanResponse();
        response.setOldmanVo(oldmanService.getOldmanByName(request.getName()));
        return response;
    }

    /**
     * 关怀系统紧急报警 根据长者关怀系统 的老人oid（最好录入不能重名）
     * @param gatewayId 长者关怀系统 的老人 网关id
     * @param type
     * @param content
     */
    @ResponseBody
    @GetMapping("/alarm")
    public ResultResponse sensorUrgency(Integer gatewayId,String type,String content) throws UnsupportedEncodingException {
        oldmanService.alarm(gatewayId,type,content);
        return new ResultResponse();
    }

    /**
     * 获取老人表 符合某个属性的数量
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCountOfOldmanField")
    public GetCountResponse getCountOfOldmanField(GetCountOfOldmanFieldRequest request){
        GetCountResponse response = new GetCountResponse();
        return response;
    }


    /**
     * 获取 根据区域分组的 各数量
     * @return
     */
    @ResponseBody
    @RequestMapping("/getOldmanGroupCount")
    public GetCountResponse getOldmanGroupCount(@RequestBody GetGroupCountRequest request){
        GetCountResponse response= new GetCountResponse();
        response.setCountMap(oldmanService.getGroupCount(request.getGroupFieldName()));
        response.sum();
        response.sortByKey(true);
        return response;
    }

    /**
     * 根据location id更新老人状态
     */
    @ResponseBody
    @RequestMapping("/updateStatusByLocationId")
    public ResultResponse updateStatusByLocationId(@RequestBody UpdateStatusByLocationIdRequest request){
        oldmanService.updateStatusByLocationId(request.getLocationId(),request.getStatus());
        return new ResultResponse();
    }

    @ResponseBody
    @RequestMapping("/getBirthdayOldman")
    public GetOldmanListResponse getBirthdayOldman(@RequestBody GetBirthdayOldmanRequest request){
        GetOldmanListResponse response=new GetOldmanListResponse();
        response.setOldmanVoList(oldmanService.getBirthdayOldman(request.getDate()));
        return response;
    }

    /**
     * 添加老人
     * @return
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultResponse add(@RequestBody SaveOldmanRequest request){
        ResultResponse response=new ResultResponse();
        oldmanService.addOldman(request.getOldmanParam());
        return response;
    }

    /**
     * 编辑老人
     * @return
     */
    @ResponseBody
    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveOldmanRequest request){
        ResultResponse response=new ResultResponse();
        oldmanService.editOldman(request.getOldmanParam());
        return response;
    }


    /**
     * excel导入
     * @param file
     * @return
     */
    //todo 限制  数量限制 一次1000？ 参数限制
    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public ModelAndView importExcel(@RequestParam MultipartFile file) {
        Pair<List<String>,List<List<String>>> excelData=excelHandler.parse(file,2);
        if(CollectionUtils.isNotEmpty(excelData.getSecond())) {
            oldmanService.addOldmanByExcel(excelData);
        }
        ModelAndView mv=new ModelAndView("/oldman");
        return mv;
    }
}
