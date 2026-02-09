package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.DepartmentCreateDTO;
import org.example.fuer_xitong.pojo.dto.DeptProfessionalGroupCreateDTO;
import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.vo.DepartmentVO;
import org.example.fuer_xitong.pojo.vo.DeptProfessionalGroupVO;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;

import java.util.List;

public interface DepartmentService {

    /*列举所有部门*/
    List<Department> listAll();
    /*根据keshi找到所有该科室下的人信息*/
    List<UserInformationVO> listByDepartmentName(String keshi);




/*上面开发有误*/
    void createDepartment(DepartmentCreateDTO dto,
                          String currentId,
                          Integer role);


    List<DepartmentVO> listDepartments();


    void createGroup(DeptProfessionalGroupCreateDTO dto);

    List<DeptProfessionalGroupVO> listGroupByDepartment(Integer departmentId);


}
