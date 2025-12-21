package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.mapper.DepartmentMapper;
import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;
import org.example.fuer_xitong.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmenServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> listAll() {
        return departmentMapper.listAll();
    }

    @Override
    public List<UserInformationVO> listByDepartmentName(String keshi) {
        return departmentMapper.listByDepartment(keshi);
    }
}
