package org.example.fuer_xitong.service.impl;

import org.example.fuer_xitong.exception.BaseException;
import org.example.fuer_xitong.mapper.DepartmentMapper;
import org.example.fuer_xitong.mapper.DepartmentProfessionalGroupMapper;
import org.example.fuer_xitong.mapper.UserMapper;
import org.example.fuer_xitong.pojo.dto.DepartmentCreateDTO;
import org.example.fuer_xitong.pojo.dto.DeptProfessionalGroupCreateDTO;
import org.example.fuer_xitong.pojo.entity.BaseContext;
import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.vo.DepartmentVO;
import org.example.fuer_xitong.pojo.vo.DeptProfessionalGroupVO;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;
import org.example.fuer_xitong.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
@Service
public class DepartmenServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentProfessionalGroupMapper departmentProfessionalGroupMapper;


    @Override
    public List<Department> listAll() {
        return departmentMapper.listAll();
    }

    @Override
    public List<UserInformationVO> listByDepartmentName(String keshi) {
        return departmentMapper.listByDepartment(keshi);
    }



    public void createDepartment(DepartmentCreateDTO dto,
                                 String currentId,
                                 Integer role) {

        // 1️⃣ 管理员权限校验
        if (role == null || role < 2) {
            throw new IllegalArgumentException("当前用户无权限创建科室");
        }

        // 3️⃣ 插入（create_by 存工号）
        departmentMapper.insertDepartment(dto, currentId);
    }


    public List<DepartmentVO> listDepartments() {

        String currentId = BaseContext.getCurrentId();
        Integer role = BaseContext.getCurrentRole();

        // 管理员：看全部
        if (role != null && role >= 2) {
            return departmentMapper.selectAll();
        }

        
        
        // 研究者：
        // 1️⃣ 先查自己所属科室
        String keshi = userMapper.selectKeshiByJobNumber(currentId);
        if (keshi == null || keshi.isBlank()) {
            return Collections.emptyList();
        }

        // 2️⃣ 再查 department 表，确保该科室已经被审批/管理员创建了
        return departmentMapper.selectByKeshiAndId(keshi, currentId);
    }




    @Transactional
    public void createGroup(DeptProfessionalGroupCreateDTO dto) {

        dto.setCreateBy(BaseContext.getCurrentId());
        // 统一处理路径格式
        String groupPath = dto.getGroupPath().trim();
        dto.setGroupPath(groupPath);

        int exists = departmentProfessionalGroupMapper.countByDeptAndPath(
                dto.getDepartmentId(),
                groupPath
        );

        if (exists > 0) {
            throw new BaseException("该科室下已存在相同专业组");
        }


        // 直接使用 DTO 入库
        departmentProfessionalGroupMapper.insert(dto);
    }



    public List<DeptProfessionalGroupVO> listGroupByDepartment(Integer departmentId) {
        List<DeptProfessionalGroupVO> aaa=departmentMapper.selectByDepartmentId(departmentId);
        return aaa;
    }

    }

