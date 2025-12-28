package org.example.fuer_xitong.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.fuer_xitong.pojo.minimal.InstitutionTeamMemberMinimalDTO;
import org.example.fuer_xitong.pojo.vo.InstitutionTeamMemberVO;

import java.util.List;

@Mapper
public interface InstitutionTeamMemberMapper {





    InstitutionTeamMemberVO getByMemberId(@Param("institutionId") String institutionId,
                                          @Param("institutionMemberId") String institutionMemberId);

    List<InstitutionTeamMemberVO> listByInstitutionId(@Param("institutionId") String institutionId);





    void insertInstitutionTeamMemberMinimal(InstitutionTeamMemberMinimalDTO dto);

    void updateInstitutionTeamMemberFiles(@Param("ziziId") Integer ziziId,
                                              @Param("name") String name,
                                              @Param("positions") String positions,
                                              @Param("resumePath") String resumePath,
                                              @Param("gcpPath") String gcpPath,
                                              @Param("licensePath") String licensePath);


    List<InstitutionTeamMemberVO> selectAll();


    int deleteById(@Param("ziziId") Integer ziziId);
}
