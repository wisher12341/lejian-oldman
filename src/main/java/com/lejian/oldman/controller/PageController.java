package com.lejian.oldman.controller;

import com.lejian.oldman.security.annotation.BackAdminAuth;
import com.lejian.oldman.security.annotation.BackUserAuth;
import com.lejian.oldman.security.annotation.WorkerAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller()
public class PageController {

    @BackUserAuth
    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/oldman")
    public ModelAndView oldman(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/oldmanInfo")
    public ModelAndView oldmanInfo(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman_info");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/oldmanAdd")
    public ModelAndView oldmanAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman_add_edit");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/oldmanEdit")
    public ModelAndView oldmanEdit(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/oldman_add_edit");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/worker")
    public ModelAndView worker(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/worker");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/workerInfo")
    public ModelAndView workerInfo(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/worker_info");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/workerEdit")
    public ModelAndView workerEdit(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/worker_add_edit");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/chx")
    public ModelAndView chx(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/chx");
        return mv;
    }


    @BackUserAuth
    @GetMapping("/chxEdit")
    public ModelAndView chxEdit(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/chx_add_edit");
        return mv;
    }

    @BackUserAuth
    @GetMapping("/main")
    public ModelAndView main(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main");
        return mv;
    }

    @BackAdminAuth
    @GetMapping("/visual")
    public ModelAndView visual(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/visual");
        return mv;
    }

    @WorkerAuth
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

    @GetMapping("/403")
    public ModelAndView error403(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/403");
        return mv;
    }

    @BackAdminAuth
    @GetMapping("/config/area")
    public ModelAndView configArea(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/config/area");
        return mv;
    }

    @BackAdminAuth
    @GetMapping("/config/visual")
    public ModelAndView configVisual(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/config/visual");
        return mv;
    }

    @BackAdminAuth
    @GetMapping("/user")
    public ModelAndView user(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/user");
        return mv;
    }

    @BackAdminAuth
    @GetMapping("/userAdd")
    public ModelAndView userAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/user_add_edit");
        return mv;
    }

    @BackAdminAuth
    @GetMapping("/userEdit")
    public ModelAndView userEdit(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/user_add_edit");
        return mv;
    }
}
