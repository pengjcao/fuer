package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.fuer_xitong.pojo.dto.DepartmentCreateDTO;
import org.example.fuer_xitong.pojo.entity.Department;
import org.example.fuer_xitong.pojo.vo.DepartmentVO;
import org.example.fuer_xitong.pojo.vo.DeptProfessionalGroupVO;
import org.example.fuer_xitong.pojo.vo.UserInformationVO;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department")
    List<Department> listAll();

    @Select("SELECT * FROM user_information WHERE keshi = #{keshi}")
    List<UserInformationVO> listByDepartment(String keshi);





    int insertDepartment(@Param("dto") DepartmentCreateDTO dto,
                         @Param("createBy") String createBy);

    // 管理员：查询所有
    List<DepartmentVO> selectAll();

    /**
     * 查询指定科室且 PI 为指定工号的部门（研究者可见）
     * @param keshi 科室
     * @param piId PI 工号
     */
    List<DepartmentVO> selectByKeshiAndId(@Param("keshi") String keshi,
                                          @Param("piId") String piId);



    List<DeptProfessionalGroupVO> selectByDepartmentId(
            @Param("departmentId") Integer departmentId
    );


}