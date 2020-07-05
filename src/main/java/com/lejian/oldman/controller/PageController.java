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


    @GetMapping("/main")
    public ModelAndView main(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main");
        return mv;
    }

}
