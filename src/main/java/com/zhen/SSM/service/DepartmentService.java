package com.zhen.SSM.service;

import com.zhen.SSM.dao.DepartmentMapper;
import com.zhen.SSM.pojo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> getDeps(){
        return departmentMapper.selectByExample(null);
    }
}
