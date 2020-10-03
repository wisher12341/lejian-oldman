package com.lejian.oldman.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller()
public class PageController {

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        return mv;
    }

    @GetMapping("/oldman")
    public ModelAndView oldman(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman");
        return mv;
    }

    @GetMapping("/oldmanInfo")
    public ModelAndView oldmanInfo(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman_info");
        return mv;
    }

    @GetMapping("/oldmanAdd")
    public ModelAndView oldmanAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman_add_edit");
        return mv;
    }

    @GetMapping("/oldmanEdit")
    public ModelAndView oldmanEdit(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman_add_edit");
        return mv;
    }


    @GetMapping("/worker")
    public ModelAndView worker(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/worker");
        return mv;
    }

    @GetMapping("/workerInfo")
    public ModelAndView workerInfo(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/worker_info");
        return mv;
    }

    @GetMapping("/workerEdit")
    public ModelAndView workerEdit(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/worker_add_edit");
        return mv;
    }

    @GetMapping("/main")
    public ModelAndView main(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main");
        return mv;
    }

    @GetMapping("/worker/checkin")
    public ModelAndView workerCheckin(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/worker_checkin");
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/login");
        return mv;
    }

    @GetMapping("/config/area")
    public ModelAndView configArea(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/config/area");
        return mv;
    }

    @GetMapping("/config/visual")
    public ModelAndView configVisual(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/config/visual");
        return mv;
    }
}
