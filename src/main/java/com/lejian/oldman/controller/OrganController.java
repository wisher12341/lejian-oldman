package com.lejian.oldman.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lejian.oldman.check.bo.CheckResultBo;
import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.*;
import com.lejian.oldman.handler.ExcelHandler;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.service.OrganService;
import com.lejian.oldman.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


@BackAdminAuth
@Controller
@ResponseBody
@RequestMapping("/organ")
public class OrganController {

    @Autowired
    private OrganService organService;
    @Autowired
    private ExcelHandler excelHandler;

    @RequestMapping("/getByPage")
    public GetOrganByPageResponse getUserByPage(@RequestBody GetOrganByPageRequest request){
        GetOrganByPageResponse response = new GetOrganByPageResponse();
        response.setOrganVoList(organService.getByPage(request.getPageParam()));
        if (request.getPageParam().getNeedCount()) {
            response.setCount(organService.getCount());
        }
        return response;
    }



    /**
     * 获取各类型机构服务的总人数
     * 归属地取后台配置数据
     */
    @RequestMapping("/getServiceCountGroupByType")
    public MapResponse getServiceCountGroupByType(){
        MapResponse response = new MapResponse();
        response.setMap(organService.getServiceCountGroupByType());
        return response;
    }

    /**
     * 获取同一类机构 各机构服务的总人数
     * 归属地取后台配置数据
     */
    @RequestMapping("/getOrganServiceCountByType")
    public MapResponse getOrganServiceCountByType(@RequestBody GetOrganByPageRequest request){
        MapResponse response = new MapResponse();
        response.setMap(organService.getOrganServiceCountByType(request.getOrganParam()));
        return response;
    }

    /**
     * 获取机构 服务类型的统一数据
     */
    @RequestMapping("/getServiceCountByOrgan")
    public MapResponse getServiceCountByOrgan(@RequestBody GetOrganByPageRequest request){
        MapResponse response = new MapResponse();
        response.setMap(organService.getServiceCountByOrgan(request.getOrganParam().getName()));
        return response;
    }


    /**
     * 机构服务老人列表分页查询
     * @param request
     * @return
     */
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/getServiceOldmanByPage")
    public GetOldmanByPageResponse getServiceOldmanByPage(@RequestBody GetOrganByPageRequest request){
        GetOldmanByPageResponse response = new GetOldmanByPageResponse();
        PageParam pageParam = request.getPageParam();
        response.setOldmanVoList(organService.getServiceOldmanByPage(pageParam.getPageNo(),pageParam.getPageSize(),request.getOrganParam()));
        return response;
    }
//    @RequestMapping("/add")
//    public ResultResponse add(@RequestBody SaveUserRequest request){
//        userService.add(request.getUserParam());
//        return new ResultResponse();
//    }
//
//    @RequestMapping("/edit")
//    public ResultResponse edit(@RequestBody SaveUserRequest request){
//        userService.edit(request.getUserParam());
//        return new ResultResponse();
//    }
//
//    @RequestMapping("/delete")
//    public ResultResponse delete(@RequestBody DeleteUserByIdRequest request){
//        userService.delete(request.getId());
//        return new ResultResponse();
//    }


    /**
     * 机构excel导入
     * 没有的添加  有的更新
     * @param file
     * @return
     */
    @BackUserAuth
    //todo 限制  数量限制 一次1000？ 参数限制
    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public ModelAndView importRzzExcel(@RequestParam MultipartFile file) {
        Pair<List<String>,List<List<String>>> excelData=excelHandler.parse(file,2);
        List<CheckResultBo> checkResultBoList= Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(excelData.getSecond())) {
            checkResultBoList=organService.addByExcel(excelData);
        }
        ModelAndView mv=new ModelAndView("/organ");
        mv.addObject("check",checkResultBoList);
        return mv;
    }
}
