package com.zhen.SSM.service;

import com.zhen.SSM.dao.EmployeeMapper;
import com.zhen.SSM.pojo.Employee;
import com.zhen.SSM.pojo.EmployeeExample;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    SqlSession batchSqlSession;

    //获取所有员工信息
    public List<Employee> getAllEmployee(){
        return employeeMapper.selectByExampleWithDep(null);
    }

    //添加员工
    public void addEmployee(Employee employee){
        employeeMapper.insertSelective(employee);
    }

    //通过名字查询
    public long queryEmpCountByName(String name){
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andNameEqualTo(name);
        return employeeMapper.countByExample(employeeExample);
    }

    //通过ID查询
    public Employee queryEmpById(Integer id) {
        return employeeMapper.selectByPrimaryKeyWithDep(id);
    }
    //有选择性的根据主键更新
    public void updateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    //根据id删除
    public void deleteEmpById(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }
    //通过id集合删除多个
    public void deleteEmpByIds(ArrayList<Integer> empIds) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andIdIn(empIds);
        EmployeeMapper mapper = batchSqlSession.getMapper(EmployeeMapper.class);
        mapper.deleteByExample(employeeExample);
    }
}
