package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department")
    List<Department> listAll();

    @Select("SELECT * FROM user_information WHERE keshi = #{keshi}")
    List<UserInformationVO> listByDepartment(String keshi);
}