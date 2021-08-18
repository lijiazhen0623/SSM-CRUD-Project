package com.zhen.SSM.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhen.SSM.pojo.Employee;
import com.zhen.SSM.pojo.Message;
import com.zhen.SSM.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 获取员工信息并进行分页处理
     *
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("/emp")
    @ResponseBody
    public Message employees(@RequestParam(value = "pn", defaultValue = "1") Integer page, Model model) {
        //PageHelper.startPage对后面紧跟的查询进行分页处理
        //每页显示10条数据
        PageHelper.startPage(page, 10);
        List<Employee> allEmployee = employeeService.getAllEmployee();
        // 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了。
        // 封装了详细的分页信息,包括有我们查询出来的数据，传入分页条连续显示的页数
        PageInfo pageInfo = new PageInfo(allEmployee, 5);
        model.addAttribute("pageInfo", pageInfo);
        return Message.Success().add("pageInfo", pageInfo);
    }

    /**
     * 添加员工，进行数据校验并返回处理结果
     * 1、支持JSR303校验
     * 2、导入Hibernate-Validator(不能导入6.多以上的包，不然没有用)
     * 请求：/emp
     * method: delete-删除  get-获取信息 post-添加员工  put-更新员工信息
     * <p>
     * 1、在需要校验的对象前添加@Valid注解开启校验功能，
     * 在被校验的对象之后添加BindingResult对象可以获取校验结果
     * 2、bindingResult.hasErrors()判断是否校验通过，
     * 校验未通过，bindingResult.getFieldError().getDefaultMessage()
     * 获取在TestEntity的属性设置的自定义message，
     * 如果没有设置，则返回默认值"javax.validation.constraints.XXX.message"
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Message insertEmp(@Valid Employee employee, BindingResult result) {
        //进行数据校验的一个判断，通过就添加数据进数据库，否则返回失败返回的message
        /*
        result.hasErrors():判断是否有错误
        result.getFieldErrors()获取错误的结果集
         */
        if (result.hasErrors()) {
            HashMap<String, Object> error = new HashMap<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                System.out.println("错误字段" + fieldError.getField());
                System.out.println("错误信息：" + fieldError.getDefaultMessage());
                System.out.println("类名" + fieldError.getObjectName());
                error.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return Message.Fail().add("error", error);
        } else {
            employeeService.addEmployee(employee);
            return Message.Success();
        }
    }

    /**
     * 通过用户名查询该用户名是否可用
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/queryName", method = RequestMethod.GET)
    @ResponseBody
    public Message queryName(String name) {
        long count = employeeService.queryEmpCountByName(name);
        if (count > 0) {
            return Message.Fail().add("name", "该用户名不可用");
        } else {
            return Message.Success();
        }
    }

    /**
     * 通过Id查询员工信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Message queryEmpById(@PathVariable(value = "id") Integer id) {
        Employee employee = employeeService.queryEmpById(id);
        return Message.Success().add("employee", employee);
    }

    /**
     * 如果直接发送ajax=PUT形式的请求
     * * 封装的数据
     * * Employee
     * * [empId=1014, empName=null, gender=null, email=null, dId=null]
     * *
     * * 问题：
     * * 请求体中有数据；
     * * 但是Employee对象封装不上；
     * * update tbl_emp  where emp_id = 1014;
     * *
     * * 原因：
     * * Tomcat：
     * * 		1、将请求体中的数据，封装一个map。
     * * 		2、request.getParameter("empName")就会从这个map中取值。
     * * 		3、SpringMVC封装POJO对象的时候。
     * * 				会把POJO中每个属性的值，request.getParamter("email");
     * * AJAX发送PUT请求引发的血案：
     * * 		PUT请求，请求体中的数据，request.getParameter("empName")拿不到
     * * 		Tomcat一看是PUT不会封装请求体中的数据为map，只有POST形式的请求才封装请求体为map
     * * org.apache.catalina.connector.Request--parseParameters() (3111);
     * *
     * * protected String parseBodyMethods = "POST";
     * * if( !getConnector().isParseBodyMethod(getMethod()) ) {
     * success = true;
     * return;
     * }
     * *
     * *
     * * 解决方案；
     * * 我们要能支持直接发送PUT、DELETE的请求还要封装请求体中的数据
     * * 1、在web.xml中配置上HttpPutFormContentFilter(已经过时)，用FormContentFilter；
     * * 2、他的作用:将请求体中的数据解析包装成一个map。
     * * 3、request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
     * * 员工更新方法
     *
     * @return
     */
    //路径中的占位符名字跟pojo里面的属性名字一样的话，SpringMVC会自动将其封装进方法的pojo参数中
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Message updateEmp(Employee employee) {
        employeeService.updateEmp(employee);
        System.out.println(employee);
        return Message.Success();
    }

    /**
     * 单个删除和多个删除一起
     * 如果是多个删除的话，每个Id之间是用‘-’连接的
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public Message deleteEmpById(@PathVariable("ids") String ids) {
        if (ids.contains("-")) {
            ArrayList<Integer> empIds = new ArrayList<>();
            //‘-’当作分割符
            for (String s : ids.split("-")) {
                empIds.add(Integer.parseInt(s));
            }
            employeeService.deleteEmpByIds(empIds);

        } else {
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmpById(id);
        }
        return Message.Success();
    }

}
