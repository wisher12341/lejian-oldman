package com.lejian.oldman.controller;

import com.lejian.oldman.vo.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/oldman")
public class OldmanController {


    @ResponseBody
    @RequestMapping("/getOldmanByPage")
    public ResponseVo getOldmanByPage(){
        return new ResponseVo();
    }
}
