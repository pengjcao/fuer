package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.dto.DeptProfessionalGroupCreateDTO;

@Mapper
public interface DepartmentProfessionalGroupMapper {
    int countByDeptAndPath(
            @Param("departmentId") Integer departmentId,
            @Param("groupPath") String groupPath
    );

    int insert(DeptProfessionalGroupCreateDTO dto);
}
