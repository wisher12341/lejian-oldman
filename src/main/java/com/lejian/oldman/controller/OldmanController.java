package com.lejian.oldman.controller;

import com.google.common.collect.Lists;
import com.lejian.oldman.bo.ExportOldmanBo;
import com.lejian.oldman.check.bo.CheckResultBo;
import com.lejian.oldman.controller.contract.request.*;
import com.lejian.oldman.controller.contract.response.*;
import com.lejian.oldman.handler.ExcelHandler;
import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.security.annotation.WorkerAndBackAuth;
import com.lejian.oldman.security.annotation.WorkerAuth;
import com.lejian.oldman.service.OldmanService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

//todo spring security 到post接口 如果没有权限的话 会报405
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
    @BackUserAuth
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
    @BackUserAuth
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
    @WorkerAndBackAuth
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
    @BackUserAuth
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
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/getOldmanByName")
    public GetOldmanResponse getOldmanByName(@RequestBody GetOldmanByNameRequest request){
        GetOldmanResponse response = new GetOldmanResponse();
        response.setOldmanVo(oldmanService.getOldmanByName(request.getName()));
        return response;
    }

    /**
     * 关怀系统紧急报警 根据长者关怀系统 的老人网关id
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
    @BackAdminAuth
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
    @BackAdminAuth
    @ResponseBody
    @RequestMapping("/getOldmanAreaGroupCount")
    public GetCountResponse getOldmanAreaGroupCount(@RequestBody GetOldmanAreaGroupCountRequest request){
        GetCountResponse response= new GetCountResponse();
        response.setCountMap(oldmanService.getOldmanAreaGroupCount(
                request.getAreaCountry(),request.getAreaTown(),request.getAreaVillage()));
        response.sum();
        response.sortByKey(true);
        return response;
    }

    /**
     * 根据location id更新老人状态
     */
    @BackAdminAuth
    @ResponseBody
    @RequestMapping("/updateStatusByLocationId")
    public ResultResponse updateStatusByLocationId(@RequestBody UpdateStatusByLocationIdRequest request){
        oldmanService.updateStatusByLocationId(request.getLocationId(),request.getStatus());
        return new ResultResponse();
    }
    @BackAdminAuth
    @ResponseBody
    @RequestMapping("/getBirthdayOldman")
    public GetOldmanListResponse getBirthdayOldman(@RequestBody GetBirthdayOldmanRequest request){
        GetOldmanListResponse response=new GetOldmanListResponse();
        response.setOldmanVoList(oldmanService.getBirthdayOldman(request.getDate(),request.getOldmanSearchParam()));
        return response;
    }

    /**
     * 添加老人
     * @return
     */
    @BackUserAuth
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
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/edit")
    public ResultResponse edit(@RequestBody SaveOldmanRequest request){
        ResultResponse response=new ResultResponse();
        oldmanService.editOldman(request.getOldmanParam());
        return response;
    }

    /**
     * 删除老人
     * @return
     */
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/delete")
    public ResultResponse delete(@RequestBody DeleteOldmanByOidRequest request){
        oldmanService.deleteOldman(request.getOid());
        return new ResultResponse();
    }

    /**
     * excel导入
     * 没有的添加  有的更新
     * @param file
     * @return
     */
    @BackUserAuth
    //todo 限制  数量限制 一次1000？ 参数限制
    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public ModelAndView importExcel(@RequestParam MultipartFile file) {
        Pair<List<String>,List<List<String>>> excelData=excelHandler.parse(file,2);
        List<CheckResultBo> checkResultBoList= Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(excelData.getSecond())) {
            checkResultBoList=oldmanService.addOldmanByExcel(excelData);
        }
        ModelAndView mv=new ModelAndView("/oldman");
        mv.addObject("check",checkResultBoList);
        return mv;
    }

    /**
     * 导出
     * @param
     */
    @BackUserAuth
    @RequestMapping(value = "/exportExcel",method = RequestMethod.POST)
    public void export(@RequestParam(value = "areaCountry",required = false) String areaCountry
            ,@RequestParam(value = "areaTown",required = false) String areaTown
            ,@RequestParam(value = "areaVillage",required = false) String areaVillage
            ,@RequestParam(value = "areaCustomOne",required = false) String areaCustomOne
            ,@RequestParam(value = "name",required = false) String name
            ,@RequestParam(value = "idCard",required = false) String idCard
            ,@RequestParam(value = "createTimeStart",required = false) String createTimeStart
            ,@RequestParam(value = "createTimeEnd",required = false) String createTimeEnd){
        OldmanSearchParam param = new OldmanSearchParam();
        param.setAreaVillage(areaVillage);
        param.setAreaTown(areaTown);
        param.setAreaCustomOne(areaCustomOne);
        param.setAreaCountry(areaCountry);
        param.setName(name);
        param.setIdCard(idCard);
        param.setCreateTimeEnd(createTimeEnd);
        param.setCreateTimeStart(createTimeStart);
        Pair<String[],String[][]> data=oldmanService.getExportOldmanInfo(param);
        excelHandler.export("oldman",data.getFirst(),data.getSecond());
    }

    @BackUserAuth
    @ResponseBody
    @RequestMapping(value = "/getHomeServiceMapCount",method = RequestMethod.POST)
    public MapResponse getHomeServiceMapCount(@RequestBody OldmanSearchParam oldmanSearchParam){
        MapResponse mapResponse = new MapResponse();
        mapResponse.setMap(oldmanService.getHomeServiceMapCount(oldmanSearchParam));
        return mapResponse;
    }

    @BackUserAuth
    @ResponseBody
    @RequestMapping(value = "/getPositionId",method = RequestMethod.POST)
    public ListResponse getPositionId(@RequestBody OldmanSearchParam oldmanSearchParam){
        ListResponse listResponse = new ListResponse();
        listResponse.setList(oldmanService.getPositionId(oldmanSearchParam));
        return listResponse;
    }

    @BackUserAuth
    @ResponseBody
    @RequestMapping(value = "/getEquipMapCount",method = RequestMethod.POST)
    public MapResponse getEquipMapCount(@RequestBody OldmanSearchParam oldmanSearchParam){
        MapResponse mapResponse = new MapResponse();
        mapResponse.setMap(oldmanService.getEquipMapCount(oldmanSearchParam));
        return mapResponse;
    }


    @BackUserAuth
    @ResponseBody
    @RequestMapping(value = "/getRzzCount",method = RequestMethod.POST)
    public MapResponse getRzzCount(@RequestBody OldmanSearchParam oldmanSearchParam){
        MapResponse mapResponse = new MapResponse();
        mapResponse.setMap(oldmanService.getRzzMapCount(oldmanSearchParam));
        return mapResponse;
    }

    /**
     * 认知症excel导入
     * 没有的添加  有的更新
     * @param file
     * @return
     */
    @BackUserAuth
    //todo 限制  数量限制 一次1000？ 参数限制
    @RequestMapping(value = "/importRzzExcel",method = RequestMethod.POST)
    public ModelAndView importRzzExcel(@RequestParam MultipartFile file) {
        Pair<List<String>,List<List<String>>> excelData=excelHandler.parse(file,2);
        List<CheckResultBo> checkResultBoList= Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(excelData.getSecond())) {
            checkResultBoList=oldmanService.addRzzByExcel(excelData);
        }
        ModelAndView mv=new ModelAndView("/rzz");
        mv.addObject("check",checkResultBoList);
        return mv;
    }


    /**
     * 认知症excel导入
     * 没有的添加  有的更新
     * @param file
     * @return
     */
    @BackUserAuth
    //todo 限制  数量限制 一次1000？ 参数限制
    @RequestMapping(value = "/importDbExcel",method = RequestMethod.POST)
    public ModelAndView importDbExcel(@RequestParam MultipartFile file) {
        Pair<List<String>,List<List<String>>> excelData=excelHandler.parse(file,2);
        List<CheckResultBo> checkResultBoList= Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(excelData.getSecond())) {
            checkResultBoList=oldmanService.addDbByExcel(excelData);
        }
        ModelAndView mv=new ModelAndView("/db");
        mv.addObject("check",checkResultBoList);
        return mv;
    }

    /**
     * 认知症老人列表分页查询
     * @param request
     * @return
     */
    @BackUserAuth
    @ResponseBody
    @RequestMapping("/getRzzOldmanByPage")
    public GetOldmanByPageResponse getRzzOldmanByPage(@RequestBody GetOldmanByPageRequest request){
        GetOldmanByPageResponse response = new GetOldmanByPageResponse();
        PageParam pageParam = request.getPageParam();
        response.setOldmanVoList(oldmanService.getRzzOldmanByPage(pageParam.getPageNo(),pageParam.getPageSize(),request.getOldmanSearchParam()));
        if(request.getNeedCount()) {
            response.setCount(oldmanService.getOldmanCount(request.getOldmanSearchParam()));
        }
        return response;
    }
}
