package com.zhen.SSM.controller;

import com.zhen.SSM.pojo.Message;
import com.zhen.SSM.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取department的信息
 */
@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/dep",method = RequestMethod.GET)
    @ResponseBody
    public Message getAllDeps(){
        return Message.Success().add("departments", departmentService.getDeps());
    }
}
