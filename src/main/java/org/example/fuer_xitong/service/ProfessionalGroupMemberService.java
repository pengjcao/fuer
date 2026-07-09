package org.example.fuer_xitong.service;

import org.example.fuer_xitong.pojo.dto.ProfessionalGroupMemberCreateDTO;
import org.example.fuer_xitong.pojo.vo.ProfessionalGroupMemberVO;

import java.util.List;

public interface ProfessionalGroupMemberService {
    void createMember(ProfessionalGroupMemberCreateDTO dto);

    List<ProfessionalGroupMemberVO> queryMembers(Integer departmentId, String groupPath);

    void deleteMember(Integer id);
}
