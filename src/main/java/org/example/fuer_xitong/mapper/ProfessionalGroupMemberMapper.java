package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.vo.ProfessionalGroupMemberVO;

import java.util.List;

@Mapper
public interface ProfessionalGroupMemberMapper {

    int insertMember(
            @Param("departmentId") Integer departmentId,
            @Param("keshi") String keshi,
            @Param("groupPath") String groupPath,
            @Param("personType") String personType,
            @Param("name") String name,
            @Param("academicPosition") String academicPosition,
            @Param("talentTitle") String talentTitle,
            @Param("roles") String roles,
            @Param("resumeFilePath") String resumeFilePath,
            @Param("gcpCertPath") String gcpCertPath,
            @Param("practiceCertPath") String practiceCertPath,
            @Param("createBy") String createBy
    );



    List<ProfessionalGroupMemberVO> selectByDepartmentAndGroup(
            @Param("departmentId") Integer departmentId,
            @Param("groupPath") String groupPath
    );

    ProfessionalGroupMemberVO selectById(@Param("id") Integer id);

    int deleteById(@Param("id") Integer id);
}
