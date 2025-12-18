package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;

import java.util.List;

public interface DepartmentService {

    /*列举所有部门*/
    List<Department> listAll();
    /*根据keshi找到所有该科室下的人信息*/
    List<UserInformationVO> listByDepartmentName(String keshi);
}
