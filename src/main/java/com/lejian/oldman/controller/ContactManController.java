package com.lejian.oldman.controller;

import com.lejian.oldman.controller.contract.GetAllLocationResponse;
import com.lejian.oldman.controller.contract.GetContactManByOidRequest;
import com.lejian.oldman.controller.contract.GetContactManByOidResponse;
import com.lejian.oldman.service.ContactManService;
import com.lejian.oldman.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/contact")
public class ContactManController {


    @Autowired
    private ContactManService service;

    @RequestMapping("/getContactManByOid")
    public GetContactManByOidResponse getContactManByOid(@RequestBody GetContactManByOidRequest request){
        GetContactManByOidResponse response = new GetContactManByOidResponse();
        response.setContactManVoList(service.getContactManByOid(request.getOid()));
        return response;
    }


}
